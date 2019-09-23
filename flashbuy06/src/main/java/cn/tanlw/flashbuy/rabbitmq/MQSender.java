package cn.tanlw.flashbuy.rabbitmq;

import cn.tanlw.flashbuy.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
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
}
