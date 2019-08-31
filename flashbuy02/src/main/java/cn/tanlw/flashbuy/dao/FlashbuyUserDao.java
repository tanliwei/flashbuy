package cn.tanlw.flashbuy.dao;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FlashbuyUserDao {
    @Select("select * from flashbuy_user where id = #{id}")
    public FlashbuyUser getById(@Param("id")long id);
    
}
