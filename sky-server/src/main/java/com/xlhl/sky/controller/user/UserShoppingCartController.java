package com.xlhl.sky.controller.user;

import com.xlhl.sky.dto.ShoppingCartDTO;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.user.UserShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
///user/shoppingCart/add
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车相关接口")
public class UserShoppingCartController {

    @Resource(name = "userShoppingCartServiceImpl")
    private UserShoppingCartService userShoppingCartService;


    /**
     * 查看购物车信息
     *
     * @return
     */
    @GetMapping
    @ApiOperation(value = "查看购物车信息")
    public Result list() {

        //ShoppingCart
        return Result.success();
    }

    /**
     * 添加购物车
     * /user/shoppingCart/add
     *
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加购物车")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车商品信息==>{}", shoppingCartDTO);

        userShoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
