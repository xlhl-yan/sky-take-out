package com.xlhl.sky.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.xlhl.sky.annotation.AutoFill;
import com.xlhl.sky.dto.DishPageQueryDTO;
import com.xlhl.sky.entity.Dish;
import com.xlhl.sky.enumeration.OperationType;
import com.xlhl.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     *
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    Integer addWith(Dish dish);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除指定id菜品数据
     *
     * @param dishId
     */
    void delById(@Param("dishId") Long dishId);

    /**
     * 根据id修改菜品数据
     *
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    Integer updateDishById(Dish dish);

    /**
     * 条件查询菜品和口味
     *
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);
}
