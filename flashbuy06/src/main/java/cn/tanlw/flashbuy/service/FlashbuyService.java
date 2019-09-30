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
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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

    private HashMap<Long, Boolean> goodsFlashbuyOverFlags = new HashMap<>();


    /**
     * 逻辑6 cores， 16GB 应用(jar)在本机 的测试结果(redis, rabbitmq, mysql 在阿里云1核2G的云服务器的容器内)：
     * 吞吐量162.6/sec, 1000线程 * 10次循环； 95个商品 ，无超卖;
     * 是原来的2.7倍
     */
    @Transactional
    public Result<Integer> preFlashbuy(FlashbuyUser flashbuyUser, Long goodsId) {
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
}
