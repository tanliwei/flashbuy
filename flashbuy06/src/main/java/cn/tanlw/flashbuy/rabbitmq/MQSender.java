package cn.tanlw.flashbuy.rabbitmq;

import cn.tanlw.flashbuy.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Creator Tan Liwei
 * @Date 2019/9/23 9:06
 */
@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    public void send(String queueMessage){
        log.info("Send queueMessage:" + queueMessage);
        amqpTemplate.convertAndSend(MQConfig.FLASHBUY_QUEUE, queueMessage);
    }

    public void sendFlashbuyMessage(FlashbuyMessage queueMessage){
        String msg = RedisService.beanToString(queueMessage);
        log.info("Send queueMessage:" + msg);
        amqpTemplate.convertAndSend(MQConfig.FLASHBUY_QUEUE, msg);
    }

    public void sendTopicMessage(Object topicMessage){
        String msg = RedisService.beanToString(topicMessage);
        log.info("Send topic queueMessage:" + msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY1, msg+"_1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY2, msg+"_2");

    }

    public void sendFanout(Object fanoutMessage){
        String msg = RedisService.beanToString(fanoutMessage);
        log.info("Send fanout queueMessage:" + msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "",msg+"_3");

    }

    public void sendHeaders(Object headersMessage){
        String msg = RedisService.beanToString(headersMessage);
        log.info("Send headers queueMessage:" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        Message msgObj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "",msgObj);

    }
}
