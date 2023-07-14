package com.xlhl.sky.service.user;

import com.xlhl.sky.dto.ShoppingCartDTO;

public interface UserShoppingCartService {

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
