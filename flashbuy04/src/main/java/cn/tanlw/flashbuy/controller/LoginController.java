package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.result.Result;
import cn.tanlw.flashbuy.service.FlashbuyUserService;
import cn.tanlw.flashbuy.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    FlashbuyUserService flashbuyUserService;
    
    @Autowired
    RedisService redisService;
    
    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }
    
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        log.info("doLogin"+loginVo.toString());
        //Login 
        flashbuyUserService.login(response, loginVo);
        return Result.success(true);
    }
}
