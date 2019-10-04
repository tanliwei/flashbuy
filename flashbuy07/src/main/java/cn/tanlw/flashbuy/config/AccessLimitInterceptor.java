package cn.tanlw.flashbuy.config;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.redis.AccessKey;
import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.result.CodeMsg;
import cn.tanlw.flashbuy.result.Result;
import cn.tanlw.flashbuy.service.FlashbuyUserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    FlashbuyUserService userService;
    @Autowired
    RedisService redisService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            boolean loginRequired = accessLimit.loginRequired();
            int maxCount = accessLimit.maxCount();
            String key = request.getRequestURI();//md5(添加参数)
            
            if(loginRequired){
                FlashbuyUser user = (FlashbuyUser)request.getAttribute(FlashbuyUserService.AUTH_TOKEN);
                if (user == null) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key = key+"_"+user.getId();
            }
            AccessKey accessKey = AccessKey.withExpire(seconds);
            Integer accessCount = redisService.get(accessKey, key, Integer.class);
            if (accessCount == null) {
                redisService.set(accessKey, key, 1);
            }else if(accessCount < maxCount){
                redisService.incr(accessKey, key);
            }else {
                render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
            return true;
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(codeMsg));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
