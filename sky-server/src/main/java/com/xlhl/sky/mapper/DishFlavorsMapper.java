package com.xlhl.sky.mapper;

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

    void delById(@Param("dishId") Long dishId);
}
