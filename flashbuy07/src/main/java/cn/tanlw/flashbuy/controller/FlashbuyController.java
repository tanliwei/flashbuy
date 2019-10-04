package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.result.CodeMsg;
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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

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


    @PostMapping("/{path}/do_flashbuy")
    @ResponseBody
    public Result<Integer> doFlashbuy(Model model, FlashbuyUser flashbuyUser,
                                        @RequestParam("goodsId")long goodsId,
                                      @PathParam("path") String path){
        Log.info("doFlashbuy, goodsId:"+goodsId+", user:"+flashbuyUser.toString());

        //Decrease the stock, make the order, insert a record into the FlashbuyOrder
        return flashbuyService.preFlashbuy(flashbuyUser, goodsId,path);
    }

    @GetMapping("/result")
    @ResponseBody
    public Result<Long> flashbuyResult(FlashbuyUser flashbuyUser, @RequestParam("goodsId")long goodsId){
        return flashbuyService.getFlashbuyResult(flashbuyUser.getId(), goodsId);
    }
    
    @GetMapping("/path")
    @ResponseBody
    public Result<String> getFlashbuyPath(FlashbuyUser user, 
                                          @RequestParam("goodsId") Long goodsId
            ,@RequestParam("verifyCode") String verifyCode
    ){
        boolean valid = flashbuyService.checkVerifyCode(user, goodsId, verifyCode);
        if(!valid){
            return Result.error(CodeMsg.REQUEST_ILLEAGAL);
        }
        String path = flashbuyService.createFlashbuyPath(user, goodsId);
        return Result.success(path);
    }

    @GetMapping("/verifyCode")
    @ResponseBody
    public Result<String> getFlashbuyVerifyCode(HttpServletResponse response, FlashbuyUser flashbuyUser,
                                                @RequestParam("goodsId") Long goodsId) {
        BufferedImage verifyCode = flashbuyService.createVerifyCode(flashbuyUser, goodsId);
        try {
            OutputStream out = response.getOutputStream();
            ImageIO.write(verifyCode, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.FLASHBUY_FAIL);
        }
    }
}
