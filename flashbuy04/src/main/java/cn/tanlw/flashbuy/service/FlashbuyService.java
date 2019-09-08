package cn.tanlw.flashbuy.service;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.domain.OrderInfo;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlashbuyService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Transactional
    public OrderInfo flashbuy(FlashbuyUser flashbuyUser, GoodsVo goods) {
        //Decrease the stock, make the order, insert a record into FlashbuyOrder
        goodsService.reduceStock(goods);
        //Get the orderInfo and flashbuyOrder;
        return orderService.createOrder(flashbuyUser, goods);
    }
}
