package com.xlhl.sky.controller.admin;

import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/admin/shop")
@Api(tags = "商家管理相关接口")
public class ShopAdminController {
    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource
    private RedisTemplate redisTemplate;

    public static final String SHOP_KEY = "SHOP_STATUS";

    /**
     * 管理端获取店铺的营业状态
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

    /**
     * 设置店铺营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态 1：营业 0：打样")
    public Result status(@PathVariable("status") Integer status) {

        log.info("将状态设置为：{}", status == 1 ? "营业中" : "打烊了");

        redisTemplate.opsForValue().set(SHOP_KEY, status);
        return Result.success();
    }
}
