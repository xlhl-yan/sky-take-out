package com.xlhl.sky.controller.admin;

import com.xlhl.sky.dto.DishDTO;
import com.xlhl.sky.dto.DishPageQueryDTO;
import com.xlhl.sky.entity.Dish;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.admin.DishService;
import com.xlhl.sky.vo.DishVO;
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
     * /admin/dish/status/{status}
     *
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "根据id修改菜品状态 1：起售 0：停售")
    public Result status(Long id, @PathVariable("status") Integer status) {
        log.info("{}菜品状态改为：{}", id, status == 1 ? "起售" : "停售");

        dishService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * /admin/dish/list
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<Dish>> list(@RequestParam("categoryId") Long categoryId) {
        log.info("查询此分类的菜品:{}", categoryId);

        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }

    /**
     * 根据id查询菜品及口味信息
     * /admin/dish/{id}
     *
     * @param dishId
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据菜品id查询菜品信息")
    public Result<DishVO> queryDishById(@PathVariable("id") Long dishId) {
        log.info("查询此菜品信息：{}", dishId);

        DishVO dishVO = dishService.queryByIdWithFlavor(dishId);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品及口味信息
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改菜品信息")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息：{}", dishDTO);

        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 根据id删除一个或多个菜品
     * /admin/dish
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
