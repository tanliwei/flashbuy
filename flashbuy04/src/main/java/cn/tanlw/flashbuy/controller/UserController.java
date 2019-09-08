package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.result.CodeMsg;
import cn.tanlw.flashbuy.result.Result;
import cn.tanlw.flashbuy.service.FlashbuyUserService;
import cn.tanlw.flashbuy.service.GoodsService;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    
    private static Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    FlashbuyUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    
    @RequestMapping("/info")
    @ResponseBody
    public Result<String> list(FlashbuyUser user){
        log.info("list, user:"+user);
        return new Result<String>(CodeMsg.SUCCESS);
    }
}
