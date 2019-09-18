package cn.tanlw.flashbuy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @Creator Tan Liwei
 * @Date 2019/8/29 22:06
 */
@SpringBootApplication
public class MainApplication{
    //Codes for war
//public class MainApplication extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(MainApplication.class);
//    }
}
