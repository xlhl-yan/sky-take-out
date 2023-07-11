package com.xlhl.sky.controller.admin;

import com.xlhl.sky.dto.DishDTO;
import com.xlhl.sky.dto.DishPageQueryDTO;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜品管理
 */
@RequestMapping("/admin/dish")
@RestController
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {
    @Resource(name = "dishServiceImpl")
    private DishService dishService;

    /**
     * 根据id删除一个或多个菜品
     * //admin/dish
     *
     * @param ids
     */
    @DeleteMapping
    @ApiOperation(value = "根据id删除一个或多个菜品数据")
    public Result deleteDishByIds(@RequestParam List<Long> ids) {
        log.info("批量删除菜品id:{}", ids);

        dishService.deleteDishByIds(ids);
        return Result.success();
    }

    /**
     * 分页查询菜品信息
     * /admin/dish/page
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询菜品信息")
    public Result page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询信息：{}", dishPageQueryDTO);

        return Result.success(dishService.pageQuery(dishPageQueryDTO));
    }

    /**
     * 新增菜品
     * /admin/dish
     *
     * @param dish 菜品信息
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result addDish(@RequestBody DishDTO dish) {
        log.info("新增菜品:{}", dish);

        dishService.addWithFlavor(dish);
        return Result.success();
    }
}
