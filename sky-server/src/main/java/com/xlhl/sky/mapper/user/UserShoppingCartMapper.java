package com.xlhl.sky.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlhl.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserShoppingCartMapper extends BaseMapper<ShoppingCart> {
    /**
     * 条件查询购物车
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 清空
     *
     * @param userId
     * @return
     */
    Integer clean(@Param("uid") Long userId);

    /**
     * 条件删除购物车
     *
     * @param shoppingCart
     * @return
     */
    Integer shoppingCartDecrease(ShoppingCart shoppingCart);
}
