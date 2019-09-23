package cn.tanlw.flashbuy.rabbitmq;

import cn.tanlw.flashbuy.domain.FlashbuyOrder;
import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.service.FlashbuyService;
import cn.tanlw.flashbuy.service.GoodsService;
import cn.tanlw.flashbuy.service.OrderService;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Creator Tan Liwei
 * @Date 2019/9/23 9:11
 */
@Service
public class MQReceiver {

    private static final Logger log = LoggerFactory.getLogger(MQReceiver.class);
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;
    @Autowired
    FlashbuyService flashbuyService;

    @RabbitListener(queues = MQConfig.FLASHBUY_QUEUE)
    public void receive(String queueMessage){
        log.info("Receive queueMessage:"+queueMessage);
//        FlashbuyMessage flashbuyMessage = RedisService.stringToBean(queueMessage, FlashbuyMessage.class);
//        FlashbuyUser user = flashbuyMessage.getUser();
//        long goodsId = flashbuyMessage.getGoodsId();
//        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
//        Integer stockCount = goodsVo.getStockCount();
//        if(stockCount < 0){
//            log.info("商品库存不足 goodsId:"+goodsId);
//            return ;
//        }
//        //是否秒杀成功
//        FlashbuyOrder order = orderService.getFlashbuyOrderByUserIdGoodsId(user.getId(), goodsId);
//        if(order!= null){
//            log.info("秒杀成功 userId:"+user.getId()+"_ goodsId:"+goodsId);
//            return;
//        }
//        //减库存 下订单 写入秒杀订单
//        flashbuyService.flashbuy(user, goodsVo);

    }
}
