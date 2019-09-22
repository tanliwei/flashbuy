package cn.tanlw.flashbuy.service;

import cn.tanlw.flashbuy.dao.GoodsDao;
import cn.tanlw.flashbuy.domain.FlashbuyGoods;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class GoodsService {
    private static final int NOT_STARTED = 0;
    private static final int ENDED = 2;
    private static final int ONGOING = 1;

    @Autowired
    GoodsDao goodsDao;
    
    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void getDetail(Model model, long goodsId) {
        
        GoodsVo goods = getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int flashbuyStatus = 0;
        int remainSeconds = 0;
        if(now < startAt){
            flashbuyStatus = NOT_STARTED;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if(now > endAt){
            flashbuyStatus = ENDED;
            remainSeconds = -1;
        } else {
            flashbuyStatus = ONGOING;
            remainSeconds = 0;
        }
        model.addAttribute("flashbuyStatus", flashbuyStatus);
        model.addAttribute("remainSeconds", remainSeconds);
    }

    public void reduceStock(GoodsVo goods) {
        FlashbuyGoods update = new FlashbuyGoods();
        update.setGoodsId(goods.getId());
        goodsDao.reduceStock(update);
    }
}
