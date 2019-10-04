package cn.tanlw.flashbuy.service;

import cn.tanlw.flashbuy.dao.OrderDao;
import cn.tanlw.flashbuy.domain.FlashbuyOrder;
import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.domain.OrderInfo;
import cn.tanlw.flashbuy.redis.OrderKey;
import cn.tanlw.flashbuy.redis.RedisService;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisService redisService;
    public FlashbuyOrder getFlashbuyOrderByUserIdGoodsId(long userId, long goodsId){
        return redisService.get(OrderKey.getFlashbuyOrderByUidGid, getKey(userId, goodsId), FlashbuyOrder.class);
    }

    private static String getKey(long userId, long goodsId) {
        return userId+"_"+goodsId;
    }

    //TODO 这个Transactional 实验过 能回滚 可以不用， 待进一步分析验证
//    @Transactional
    public OrderInfo createOrder(FlashbuyUser flashbuyUser, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getFlashbuyPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(flashbuyUser.getId());

        orderDao.insert(orderInfo);
        FlashbuyOrder flashbuyOrder = new FlashbuyOrder();
        flashbuyOrder.setGoodsId(goods.getId());
        flashbuyOrder.setOrderId(orderInfo.getId());
        flashbuyOrder.setUserId(flashbuyUser.getId());
        orderDao.insertFlashbuyOrder(flashbuyOrder);

        redisService.set(OrderKey.getFlashbuyOrderByUidGid, getKey(flashbuyUser.getId(), goods.getId()), flashbuyOrder);
        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
