package cn.tanlw.flashbuy.service;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrepareService {

    @Autowired
    FlashbuyUserService flashbuyUserService;
    
    public void prepareUsers() {
        FlashbuyUser user = new FlashbuyUser();
        for (int i = 0; i < 1000; i++) {
            user.setId(18322000000L + i);
            user.setNickname("user"+i);
            user.setSalt("passwordSalt"+i);
            String calcPassword = MD5Util.formPassToDBPass(
                    MD5Util.inputPassToFormPass("password"+i)
                    , user.getSalt());
            user.setPassword(calcPassword);
            flashbuyUserService.insert(user);
        }
    }
}
