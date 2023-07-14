package com.xlhl.sky.service.user.impl;

import com.xlhl.sky.context.BaseContext;
import com.xlhl.sky.dto.ShoppingCartDTO;
import com.xlhl.sky.entity.Dish;
import com.xlhl.sky.entity.SetMeal;
import com.xlhl.sky.entity.ShoppingCart;
import com.xlhl.sky.mapper.admin.DishMapper;
import com.xlhl.sky.mapper.admin.SetMealMapper;
import com.xlhl.sky.mapper.user.UserShoppingCartMapper;
import com.xlhl.sky.service.user.UserShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserShoppingCartServiceImpl implements UserShoppingCartService {

    @Resource(name = "userShoppingCartMapper")
    private UserShoppingCartMapper userShoppingCartMapper;

    @Resource(name = "dishMapper")
    private DishMapper dishMapper;

    @Resource(name = "setMealMapper")
    private SetMealMapper setMealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        assert shoppingCartDTO != null;

        //==>购物车是否存在该商品
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> shoppingCartList = userShoppingCartMapper.list(shoppingCart);

        if (shoppingCartList != null && shoppingCartList.size() > 0) { //==>存在

            //==>商品数量+1
            ShoppingCart cart = shoppingCartList.get(0);

            cart.setNumber(cart.getNumber() + 1);

            userShoppingCartMapper.updateById(cart);
        } else { //==>不存在

            //==>插入商品数据
            //==>判断商品为菜品or套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {//==>菜品
                Dish dish = dishMapper.queryDishById(dishId);

                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {//==>套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                SetMeal setMeal = setMealMapper.querySetMealById(setmealId);

                shoppingCart.setName(setMeal.getName());
                shoppingCart.setImage(setMeal.getImage());
                shoppingCart.setAmount(setMeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            //新增菜品信息
            int insert = userShoppingCartMapper.insert(shoppingCart);

            assert insert == 1;
        }
    }

    /**
     * 查看购物车信息
     *
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        ShoppingCart build = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        return userShoppingCartMapper.list(build);
    }
}
