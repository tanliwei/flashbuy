package cn.tanlw.flashbuy.dao;

import cn.tanlw.flashbuy.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {
    
    @Select("select g.*,fg.stock_count, fg.start_date, fg.end_date,fg.flashbuy_price " +
            "from flashbuy_goods fg " +
            "left join goods g on fg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();
}
