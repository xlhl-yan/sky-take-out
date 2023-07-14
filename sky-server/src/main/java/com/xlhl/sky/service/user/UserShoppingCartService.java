package com.xlhl.sky.service.user;

import com.xlhl.sky.dto.ShoppingCartDTO;
import com.xlhl.sky.entity.ShoppingCart;

import java.util.List;

public interface UserShoppingCartService {

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车信息
     *
     * @return 购物车信息
     */
    List<ShoppingCart> showShoppingCart();
}
