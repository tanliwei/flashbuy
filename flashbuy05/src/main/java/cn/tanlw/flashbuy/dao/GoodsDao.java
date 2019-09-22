package cn.tanlw.flashbuy.dao;

import cn.tanlw.flashbuy.domain.FlashbuyGoods;
import cn.tanlw.flashbuy.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsDao {
    
    @Select("select g.*,fg.stock_count, fg.start_date, fg.end_date,fg.flashbuy_price " +
            "from flashbuy_goods fg " +
            "left join goods g on fg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    @Select("select g.*,fg.stock_count, fg.start_date, fg.end_date,fg.flashbuy_price " +
            "from flashbuy_goods fg " +
            "left join goods g on fg.goods_id = g.id " +
            "where g.id = #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

    @Update("update flashbuy_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(FlashbuyGoods g);
}
