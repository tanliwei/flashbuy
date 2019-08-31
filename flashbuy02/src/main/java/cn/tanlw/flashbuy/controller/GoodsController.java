package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    
    @RequestMapping("/to_list")
    public String list(Model model, FlashbuyUser user){
        model.addAttribute("user", user);
        return "goods_list";
    }
}
