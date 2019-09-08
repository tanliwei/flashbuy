package cn.tanlw.flashbuy.controller;

import cn.tanlw.flashbuy.service.PrepareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prepare")
public class PrepareController {

    private static final Logger Log = LoggerFactory.getLogger(PrepareController.class); 

    @Autowired
    PrepareService prepareService;
    @RequestMapping("/generateUsers")
    public void generateUsers(){
        Log.info("generateUsers...");
        prepareService.prepareUsers();
    }
}
