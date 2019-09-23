package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.domain.FlashbuyOrder;
import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.domain.OrderInfo;
import cn.tanlw.flashbuy.result.CodeMsg;
import cn.tanlw.flashbuy.result.Result;
import cn.tanlw.flashbuy.service.FlashbuyService;
import cn.tanlw.flashbuy.service.GoodsService;
import cn.tanlw.flashbuy.service.OrderService;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flashbuy")
public class FlashbuyController {
    private static final Logger Log = LoggerFactory.getLogger(FlashbuyController.class);

    public static final String FLASHBUY_FAIL = "flashbuy_fail";
    public static final String ORDER_DETAIL = "order_detail";
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private FlashbuyService flashbuyService;
    @Autowired
    private OrderService orderService;


    /**
     * 逻辑6 cores， 16GB 本机 测试结果：
     * 吞吐量59.7/sec, 1000线程 * 10次循环； 100个商品 ，“卖出了”177个，超卖77个; 
     *
     * 1 core 2GB 阿里云（redis 和 mysql 在同一机器） 测试结果：
     * 吞吐量68.7/sec, 1000线程 * 10次循环； 100个商品 ，“卖出了”108个，超卖8个; 
     *
     * @param model
     * @param flashbuyUser
     * @param goodsId
     * @return
     */
    @PostMapping("/do_flashbuy")
    @ResponseBody
    public Result<OrderInfo> doFlashbuy(Model model, FlashbuyUser flashbuyUser,
                                        @RequestParam("goodsId")long goodsId){
        Log.info("doFlashbuy, goodsId:"+goodsId+", user:"+flashbuyUser.toString());
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
        //Decrease the stock, make the order, insert a record into the FlashbuyOrder
        OrderInfo orderInfo = flashbuyService.flashbuy(flashbuyUser, goods);
        model.addAttribute("orderInfo", orderInfo);
        return Result.success(orderInfo);
    }
}
