package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.service.FlashbuyUserService;
import cn.tanlw.flashbuy.service.GoodsService;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    FlashbuyUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    
    @RequestMapping("/to_list")
    public String list(Model model, FlashbuyUser user){
        model.addAttribute("user", user);
        // list the goods
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        return "goods_list";
    }
}
