package com.xlhl.sky.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.xlhl.sky.constant.StatusConstant;
import com.xlhl.sky.entity.Dish;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.admin.DishService;
import com.xlhl.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class UserDishController {
    @Resource(name = "dishServiceImpl")
    private DishService dishService;

    @Resource()
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        //==>构造redis中的key
        String key = "dish_" + categoryId;

        List<DishVO> list;

        //==>查询缓存中是否存在数据 JSON格式
        String value = (String) redisTemplate.opsForValue().get(key);

        if (StringUtils.isNotBlank(value)) {//==>缓存命中
            list = JSONObject.parseArray(value, DishVO.class);//解析JSON格式数据
            log.info("解析缓存的JSON分类数据===>{}", list.toString());
            return Result.success(list);
        }

        //查询数据库
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        list = dishService.listWithFlavor(dish);

        //==>数据转换为JSON格式数据
        String listJson = JSONObject.toJSONString(list);
        redisTemplate.opsForValue().set(key, listJson);

        return Result.success(list);
    }

}
