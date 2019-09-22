package cn.tanlw.flashbuy.dao;

import cn.tanlw.flashbuy.domain.FlashbuyOrder;
import cn.tanlw.flashbuy.domain.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface OrderDao {
    @Select("select * from flashbuy_order where user_id=#{userId} and goods_id=#{goodsId}")
    public FlashbuyOrder getFlashbuyOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty = "id", resultType = Long.class, before = false,
            statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into flashbuy_order (user_id, goods_id, order_id)" +
            " values(#{userId}, #{goodsId}, #{orderId})")
    public int insertFlashbuyOrder(FlashbuyOrder FlashbuyOrder);
}
