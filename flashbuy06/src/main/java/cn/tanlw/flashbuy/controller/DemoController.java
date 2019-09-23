package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.rabbitmq.MQSender;
import cn.tanlw.flashbuy.result.CodeMsg;
import cn.tanlw.flashbuy.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Creator Tan Liwei
 * @Date 2019/8/29 22:02
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    MQSender mqSender;

    @GetMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hello, imooc");
    }

    @GetMapping("/error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "Daniel");
        return "hello";
    }

    @RequestMapping("/mqsend")
    @ResponseBody
    public Result<String> mqSend(){
        mqSender.send("Hello RabbitMQ!");
        return Result.success("Success");
    }
}
