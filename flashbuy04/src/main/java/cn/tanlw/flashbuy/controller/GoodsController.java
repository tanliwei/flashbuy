package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.service.FlashbuyUserService;
import cn.tanlw.flashbuy.service.GoodsService;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    
    private static Logger log = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    FlashbuyUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    
    @RequestMapping("/to_list")
    public String list(Model model, FlashbuyUser user){
        log.info("list, user:"+user.toString());
        model.addAttribute("user", user);
        // list the goods
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, FlashbuyUser user, @PathVariable("goodsId") long goodsId){
        log.info("detail, user:"+user.toString()+" goodsId:"+goodsId);
        model.addAttribute("user", user);
        goodsService.getDetail(model, goodsId);
        return "goods_detail";
    }
}
