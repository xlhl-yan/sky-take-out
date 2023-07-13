package com.xlhl.sky.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlhl.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DishFlavorsMapper extends BaseMapper<DishFlavor> {
    /**
     * 批量插入口味数据
     *
     * @param flavorsList
     */
    void addFlavors(List<DishFlavor> flavorsList);

    /**
     * 根据id删除口味
     *
     * @param dishId
     */
    void delById(@Param("dishId") Long dishId);

    /**
     * 根据菜品id查询口味信息
     *
     * @param id
     * @return
     */
    List<DishFlavor> queryDishFlavorsByDishId(@Param("id") Long id);
}
