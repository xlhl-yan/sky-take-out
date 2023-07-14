package com.xlhl.sky.controller.admin;

import com.xlhl.sky.constant.StatusConstant;
import com.xlhl.sky.dto.SetMealDTO;
import com.xlhl.sky.dto.SetMealPageQueryDTO;
import com.xlhl.sky.entity.SetMeal;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.admin.SetMealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetMealController {
    @Resource(name = "setMealServiceImpl")
    private SetMealService setMealService;

    /**
     * 根据分类id查询套餐信息
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "SetMealCache", key = "#categoryId")
    @ApiOperation(value = "根据分类id查询套餐信息")
    public Result<List<SetMeal>> list(Long categoryId) {
        log.info("查询此分类套餐==>{}", categoryId);
        SetMeal setMeal = new SetMeal();
        setMeal.setCategoryId(categoryId);
        setMeal.setStatus(StatusConstant.ENABLE);

        List<SetMeal> list = setMealService.list(setMeal);
        return Result.success(list);
    }

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐信息")
    @Cacheable(cacheNames = "SetMealCache", key = "#id")
    public Result<SetMeal> querySetMealById(@PathVariable("id") Integer id) {
        log.info("查询套餐信息id==>{}", id);
        SetMeal setMeal = setMealService.querySetMealById(id);

        return Result.success(setMeal);
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @GetMapping
    @ApiOperation(value = "批量删除单个或多个套餐")
    @CacheEvict(cacheNames = "SetMealCache", allEntries = true)
    public Result delSetMealById(@RequestParam List<Long> ids) {
        log.info("批量删除菜品id:{}", ids);

        setMealService.delSetMealById(ids);

        return Result.success();
    }

    /**
     * 修改套餐状态
     * /admin/setmeal/status/{status}
     *
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "修改套餐状态 1：起售 0：停售")
    @CacheEvict(cacheNames = "SetMealCache", key = "#id")
    public Result updateStatus(@PathVariable("status") Integer status, Long id) {
        setMealService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 分页查询套餐信息
     * /admin/setmeal/page
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询套餐信息")
    @Cacheable(cacheNames = "SetMealCache", key = "#setMealPageQueryDTO.page")
    public Result<PageResult> page(SetMealPageQueryDTO setMealPageQueryDTO) {
        log.info("分页查询套餐信息。。。");
        PageResult page = setMealService.page(setMealPageQueryDTO);
        return Result.success(page);
    }

    /**
     * 修改套餐信息
     *
     * @param setMealDTO
     * @return
     */
    @PutMapping
    @CachePut(cacheNames = "SetMealCache", key = "#setMealDTO.id")
    @ApiOperation(value = "修改套餐信息")
    public Result updateSetMeal(SetMealDTO setMealDTO) {
        log.info("修改套餐信息==>{}", setMealDTO);

        setMealService.updateSetMeal(setMealDTO);

        return Result.success();
    }

    /**
     * 新增套餐信息
     *
     * @param setMealDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增套餐信息")
    @CachePut(cacheNames = "SetMealCache", key = "#setMealDTO.id")
    public Result addSetMeal(@RequestBody SetMealDTO setMealDTO) {
        log.info("新增套餐为==>{}", setMealDTO);

        setMealService.addSetMeal(setMealDTO);

        return Result.success();
    }
}
