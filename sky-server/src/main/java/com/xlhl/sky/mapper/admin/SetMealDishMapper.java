package com.xlhl.sky.mapper.admin;

import com.xlhl.sky.entity.SetMealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SetMealDishMapper {
    /**
     * 根据菜品id查询套餐id
     *
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsByIds(List<Long> dishIds);

    /**
     * 新增套餐关联的菜品信息
     *
     * @param setMealDishes
     * @return
     */
    Integer addSetMealDish(List<SetMealDish> setMealDishes);

    /**
     * 根据套餐id查询菜品信息
     *
     * @param setMealId
     * @return
     */
    List<SetMealDish> getSetMealDishById(@Param("setMealId") Long setMealId);

    /**
     * 根据套餐id删除关联信息
     *
     * @param setMealId
     */
    Integer delSetMealDishBySetMealId(@Param("setMealId") Long setMealId);
}
