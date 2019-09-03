package cn.tanlw.flashbuy.service;

import cn.tanlw.flashbuy.dao.GoodsDao;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;
    
    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }
}
