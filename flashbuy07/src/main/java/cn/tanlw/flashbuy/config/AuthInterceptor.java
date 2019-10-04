package cn.tanlw.flashbuy.config;


import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.service.FlashbuyUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private FlashbuyUserService flashbuyUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String paramToken = request.getParameter(FlashbuyUserService.AUTH_TOKEN);
        String cookieToken = getCookieValue(request, FlashbuyUserService.AUTH_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return doAuthFail(request, response);
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        FlashbuyUser flashbuyUser = flashbuyUserService.getByToken(response, token);
        if(flashbuyUser==null){
            return doAuthFail(request, response);
        }
        request.setAttribute(FlashbuyUserService.AUTH_TOKEN, flashbuyUser);
        return true;
    }

    private boolean doAuthFail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/login/to_login";
        request.getRequestDispatcher(url).forward(request,response);
        return false;
    }

    private String getCookieValue(HttpServletRequest request, String authToken) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(authToken)) {
                return cookie.getValue();
            }
        }
        return null;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
