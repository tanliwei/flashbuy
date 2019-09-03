package cn.tanlw.flashbuy.service;

import cn.tanlw.flashbuy.dao.FlashbuyUserDao;
import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.exception.GlobalException;
import cn.tanlw.flashbuy.redis.FlashbuyUserKey;
import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.result.CodeMsg;
import cn.tanlw.flashbuy.util.MD5Util;
import cn.tanlw.flashbuy.util.UUIDUtil;
import cn.tanlw.flashbuy.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class FlashbuyUserService {
    public static final String AUTH_TOKEN = "auth_token";
    @Autowired
    FlashbuyUserDao flashbuyUserDao;
    
    @Autowired
    RedisService redisService;

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPassword = loginVo.getPassword();
        //Validating the phone
        FlashbuyUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //Validating the password 
        String dbPassword = user.getPassword();
        String salt = user.getSalt();
        String calcPassword = MD5Util.formPassToDBPass(formPassword, salt);
        if(!calcPassword.equals(dbPassword)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //Setting the cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, FlashbuyUser user) {
        redisService.set(FlashbuyUserKey.AUTH_TOKEN, token, user);
        Cookie cookie = new Cookie(AUTH_TOKEN, token);
        cookie.setMaxAge(FlashbuyUserKey.AUTH_TOKEN.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private FlashbuyUser getById(long id) {
        return flashbuyUserDao.getById(id);
    }

    public FlashbuyUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        FlashbuyUser user = redisService.get(FlashbuyUserKey.AUTH_TOKEN, token, FlashbuyUser.class);
        //Extend the expire date
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
}
