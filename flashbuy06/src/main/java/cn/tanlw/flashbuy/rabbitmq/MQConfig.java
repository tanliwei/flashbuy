package cn.tanlw.flashbuy.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Creator Tan Liwei
 * @Date 2019/9/23 9:10
 */
@Configuration
public class MQConfig {
    public static final String FLASHBUY_QUEUE = "Flashbuy.queue";

    @Bean
    public Queue flashbuyQueue(){
        return new Queue(FLASHBUY_QUEUE, true);
    }

}
