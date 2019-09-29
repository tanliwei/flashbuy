package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.result.Result;
import cn.tanlw.flashbuy.service.FlashbuyService;
import cn.tanlw.flashbuy.service.GoodsService;
import cn.tanlw.flashbuy.service.OrderService;
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


    @PostMapping("/do_flashbuy")
    @ResponseBody
    public Result<Integer> doFlashbuy(Model model, FlashbuyUser flashbuyUser,
                                        @RequestParam("goodsId")long goodsId){
        Log.info("doFlashbuy, goodsId:"+goodsId+", user:"+flashbuyUser.toString());

        //Decrease the stock, make the order, insert a record into the FlashbuyOrder
        return flashbuyService.preFlashbuy(flashbuyUser, goodsId);
    }

    @GetMapping("/result")
    @ResponseBody
    public Result<Long> flashbuyResult(FlashbuyUser flashbuyUser, @RequestParam("goodsId")long goodsId){
        return flashbuyService.getFlashbuyResult(flashbuyUser.getId(), goodsId);
    }
}
