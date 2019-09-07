package cn.tanlw.flashbuy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    FlashbuyUserArgumentResolver argumentResolver;
    @Autowired
    AuthInterceptor authInterceptor;
   
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(argumentResolver);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*bomekkkk：调用我们创建的SessionInterceptor。
         * addPathPatterns("/api/**)的意思是这个链接下的都要进入到SessionInterceptor里面去执行
         * excludePathPatterns("/login")的意思是login的url可以不用进入到SessionInterceptor中，直接
         * 放过执行。
         *
         * 注意：如果像注释那样写是不可以的。这样等于是创建了多个Interceptor。而不是只有一个Interceptor
         * 所以这里有个大坑，搞了很久才发现问题。
         *
         * */
//        AuthInterceptor authInterceptor=new AuthInterceptor();
        registry.addInterceptor(authInterceptor).addPathPatterns("/flashbuy/**","/goods/**")
                .excludePathPatterns("/login","/verify");
//        registry.addInterceptor(AuthInterceptor).excludePathPatterns("/login");
//        registry.addInterceptor(AuthInterceptor).excludePathPatterns("/verify");
        super.addInterceptors(registry);
    }
}
