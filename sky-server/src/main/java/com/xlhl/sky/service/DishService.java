package com.xlhl.sky.service;

import com.xlhl.sky.dto.DishDTO;
import com.xlhl.sky.dto.DishPageQueryDTO;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.result.Result;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品与口味
     *
     * @param dish
     */
    Result addWithFlavor(DishDTO dish);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id删除一个或多个菜品
     * @param ids
     */
    void deleteDishByIds(List<Long> ids);
}
