package cn.tanlw.flashbuy.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Creator Tan Liwei
 * @Date 2019/9/23 9:10
 */
@Configuration
public class MQConfig {
    public static final String FLASHBUY_QUEUE = "Flashbuy.queue";
    public static final String TOPIC_QUEUE1 = "Topic.queue1";
    public static final String TOPIC_QUEUE2 = "Topic.queue2";
    public static final String TOPIC_EXCHANGE = "Topic.exchange";
    public static final String ROUTING_KEY1 = "Topic.routingkey1";
    public static final String ROUTING_KEY2 = "Topic.#";
    public static final String FANOUT_EXCHANGE = "FanoutExchange";
    public static final String HEADERS_EXCHANGE = "HeadersExchange";
    public static final String HEADERS_QUEUE = "Headers.queue";

    /**
     * Direct模式 默认Exchange
     * RabbitMQ有四种交换机Exchange
     * 发送者发消息 -> 交换机 -> Queue
     */
    @Bean
    public Queue flashbuyQueue(){
        return new Queue(FLASHBUY_QUEUE, true);
    }

    /**
     * Topic模式
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1, true);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2, true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * Binding 把 Queue 和 Exchange 绑定在一起
     * @return
     */
    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }
    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }

    /**
     * Fanout模式 广播模式
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Binding FanoutBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding FanoutBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /**
     * Header模式
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue headerQueue(){
        return new Queue(HEADERS_QUEUE, true);
    }
    @Bean
    public Binding headerBinding(){
        Map<String, Object> map = new HashMap<>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
    }
}
