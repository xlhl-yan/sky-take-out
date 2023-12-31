package com.xlhl.sky.service.admin;

import com.xlhl.sky.dto.DishDTO;
import com.xlhl.sky.dto.DishPageQueryDTO;
import com.xlhl.sky.entity.Dish;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.vo.DishVO;

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
     *
     * @param ids
     */
    void deleteDishByIds(List<Long> ids);

    /**
     * 根据id查询菜品及口味数据
     *
     * @param dishId
     * @return
     */
    DishVO queryByIdWithFlavor(Long dishId);

    /**
     * 根据id修改对应的菜品及口味信息
     *
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 条件查询菜品与口味
     *
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * 改变菜品起售停售状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);
}
