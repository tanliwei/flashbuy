package cn.tanlw.flashbuy.service;

import cn.tanlw.flashbuy.domain.FlashbuyOrder;
import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.domain.OrderInfo;
import cn.tanlw.flashbuy.rabbitmq.FlashbuyMessage;
import cn.tanlw.flashbuy.rabbitmq.MQSender;
import cn.tanlw.flashbuy.redis.FlashbuyKey;
import cn.tanlw.flashbuy.redis.GoodsKey;
import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.result.CodeMsg;
import cn.tanlw.flashbuy.result.Result;
import cn.tanlw.flashbuy.util.MD5Util;
import cn.tanlw.flashbuy.util.UUIDUtil;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class FlashbuyService implements InitializingBean{
    public static final int IN_QUEUE = 0;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQSender mqSender;

    private HashMap<Long, Boolean> goodsFlashbuyOverFlags = new HashMap<Long, Boolean>();


    /**
     * 逻辑6 cores， 16GB 应用(jar)在本机 的测试结果(redis, rabbitmq, mysql 在阿里云1核2G的云服务器的容器内)：
     * 吞吐量162.6/sec, 1000线程 * 10次循环； 95个商品 ，无超卖;
     * 是原来的2.7倍
     */
    @Transactional
    public Result<Integer> preFlashbuy(FlashbuyUser flashbuyUser, Long goodsId, String path) {
        //Checking the path
        if (goodsId == null) {
            return Result.error(CodeMsg.REQUEST_ILLEAGAL);
        }
        String correctPath = redisService.get(FlashbuyKey.getFlashbuyPath, flashbuyUser.getId() + "_" + goodsId, String.class);
        if (!correctPath.equals(path)) {
            return Result.error(CodeMsg.REQUEST_ILLEAGAL);
        }
        //Checking the stock
        Boolean isOver = goodsFlashbuyOverFlags.get(goodsId);
        if(isOver){
            return Result.error(CodeMsg.FLASHBUY_OVER);
        }
        Long stock = redisService.decr(GoodsKey.getFlashbuyGoodsStock, "" + goodsId);
        if(stock<0){
            goodsFlashbuyOverFlags.put(goodsId,true);
            return Result.error(CodeMsg.FLASHBUY_OVER);
        }
        //Checking whether done the flash buy
        FlashbuyOrder order = orderService.getFlashbuyOrderByUserIdGoodsId(flashbuyUser.getId(),
                goodsId);
        if (order != null) {
            redisService.incr(GoodsKey.getFlashbuyGoodsStock, "" + goodsId);
            return Result.error(CodeMsg.FLASHBUY_REPEATED);
        }
        //Enqueue
        FlashbuyMessage flashbuyMessage = new FlashbuyMessage();
        flashbuyMessage.setGoodsId(goodsId);
        flashbuyMessage.setUser(flashbuyUser);
        mqSender.sendFlashbuyMessage(flashbuyMessage);
        return Result.success(IN_QUEUE);
    }

    /**
     * 逻辑6 cores， 16GB 应用(jar)在本机 的测试结果(redis, rabbitmq, mysql 在阿里云1核2G的云服务器的容器内)：
     * 吞吐量59.7/sec, 1000线程 * 10次循环； 100个商品 ，“卖出了”177个，超卖77个;
     *
     * 1 core 2GB 阿里云（jar, redis, rabbitmq, mysql 都在阿里云上） 测试结果：
     * 吞吐量68.7/sec, 1000线程 * 10次循环； 100个商品 ，“卖出了”108个，超卖8个;
     *
     * @param flashbuyUser
     * @param goodsId
     * @return
     */
    @Transactional
    public Result<OrderInfo> flashbuyBAK(FlashbuyUser flashbuyUser, Long goodsId) {
        //Checking the stock
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);//刷订单 重复 请求, 防止不了并发!
        Integer stockCount = goods.getStockCount();
        if(stockCount <= 0){
            return Result.error(CodeMsg.FLASHBUY_OVER);
        }
        //Checking whether done the flash buy
        FlashbuyOrder order = orderService.getFlashbuyOrderByUserIdGoodsId(flashbuyUser.getId(),
                goodsId);
        if (order != null) {
            return Result.error(CodeMsg.FLASHBUY_REPEATED);
        }
        //Decrease the stock, make the order, insert a record into FlashbuyOrder
        int i = goodsService.reduceStock(goods);
        if (i<=0){
            throw new RuntimeException("Flashbuy fails");
        }

        //Get the orderInfo and flashbuyOrder;
        OrderInfo orderInfo = orderService.createOrder(flashbuyUser, goods);
        return Result.success(orderInfo);
    }
    @Transactional
    public OrderInfo flashbuyBAK(FlashbuyUser flashbuyUser, GoodsVo goods) {
        //Decrease the stock, make the order, insert a record into FlashbuyOrder
        goodsService.reduceStock(goods);
        //Get the orderInfo and flashbuyOrder;
        return orderService.createOrder(flashbuyUser, goods);
    }

    @Transactional
    public OrderInfo doFlashbuy(FlashbuyUser user, GoodsVo goodsVo) {
        int i = goodsService.reduceStock(goodsVo);
        //Flash buy fails
        if(i<=0){
            setGoodsFlashbuyOver(goodsVo);
            return null;
        }
        return orderService.createOrder(user, goodsVo);
    }

    private void setGoodsFlashbuyOver(GoodsVo goodsVo) {
        redisService.set(FlashbuyKey.goodsFlashbuyOver, "" + goodsVo.getId(), true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if (goodsVos == null) {
            return;
        }
        for (GoodsVo goods: goodsVos) {
            redisService.set(GoodsKey.getFlashbuyGoodsStock,"" + goods.getId(), goods.getStockCount());
            goodsFlashbuyOverFlags.put(goods.getId(), false);
        }
    }

    public Result<Long> getFlashbuyResult(Long userId, long goodsId) {
        FlashbuyOrder flashbuyOrder = orderService.getFlashbuyOrderByUserIdGoodsId(userId, goodsId);
        if (flashbuyOrder == null) {
            if(goodsFlashbuyOver(goodsId)){
                return Result.success(-1L);
            }
            return Result.success(0L);
        }else{
            return Result.success(flashbuyOrder.getOrderId());
        }

    }

    private boolean goodsFlashbuyOver(long goodsId) {
        return redisService.exists(FlashbuyKey.goodsFlashbuyOver,""+goodsId);
    }

    public boolean checkVerifyCode(FlashbuyUser user, Long goodsId, String verifyCode) {
        if(goodsId <=0 )
            return false;
        Long correctCode = redisService.get(FlashbuyKey.getFlashbuyVerifyCode, user.getId() + "_" + goodsId, Long.class);
        if (correctCode == null || !(correctCode.equals(new Long(verifyCode)))) {
            return false;
        }
        //资源回收 资源有限
        redisService.delete(FlashbuyKey.getFlashbuyVerifyCode, user.getId() + "_" + goodsId);
        return true;
    }

    public String createFlashbuyPath(FlashbuyUser user, Long goodsId) {
        if (goodsId == null) {
            return null;
        }
        String path = MD5Util.md5(UUIDUtil.uuid() + "8383720");
        redisService.set(FlashbuyKey.getFlashbuyPath, user.getId() + "_" + goodsId, path);
        return path;
    }

    public BufferedImage createVerifyCode(FlashbuyUser flashbuyUser, Long goodsId) {
        if (goodsId == null) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String exp = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(exp, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int calcResult = calc(exp);
        redisService.set(FlashbuyKey.getFlashbuyVerifyCode, flashbuyUser.getId()+"_"+goodsId, calcResult);
        //输出图片	
        return image;
    }

    private int calc(String exp) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        try {
            return (Integer)engine.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }


    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - * 
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }
}
