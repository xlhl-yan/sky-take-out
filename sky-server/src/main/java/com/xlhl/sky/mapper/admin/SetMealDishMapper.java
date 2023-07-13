package com.xlhl.sky.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
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
}
