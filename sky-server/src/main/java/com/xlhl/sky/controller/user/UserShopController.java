package com.xlhl.sky.controller.user;

import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/user/shop")
@RestController
@Api(tags = "用户查询店铺")
@Slf4j
public class UserShopController {

    public static final String SHOP_KEY = "SHOP_STATUS";

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 用户端获取店铺的营业状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查询店铺营业状态 1：营业 0：打样")
    public Result<Integer> status() {
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_KEY);

        if (status == null) {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }

        log.info("当前营业状态是：{}", status == 1 ? "营业中" : "打烊了");

        return Result.success(status);
    }
}
