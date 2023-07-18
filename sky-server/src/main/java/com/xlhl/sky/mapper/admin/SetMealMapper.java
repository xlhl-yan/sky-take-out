package com.xlhl.sky.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.xlhl.sky.dto.SetMealPageQueryDTO;
import com.xlhl.sky.entity.SetMeal;
import com.xlhl.sky.vo.DishItemVO;
import com.xlhl.sky.vo.SetMealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SetMealMapper extends BaseMapper<SetMeal> {

    /**
     * 根据条件统计套餐数量
     *
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 动态条件查询套餐
     *
     * @param setmeal
     * @return
     */
    List<SetMeal> list(SetMeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     *
     * @param setMealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setMealId}")
    List<DishItemVO> getDishItemBySetMealId(@Param("setMealId") Long setMealId);


    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐信息
     *
     * @param setMeal
     * @return
     */
    Integer addSetMeal(SetMeal setMeal);

    /**
     * 修改套餐信息
     *
     * @param setMeal
     * @return
     */
    Integer updateSetMeal(SetMeal setMeal);

    /**
     * 分页查询套餐信息
     *
     * @param setMealPageQueryDTO
     * @return
     */
    Page<SetMealVO> page(SetMealPageQueryDTO setMealPageQueryDTO);

    /**
     * 修改套餐状态
     *
     * @param status
     * @param id
     * @return
     */
    Integer updateStatus(@Param("status") Integer status,
                         @Param("id") Long id);

    /**
     * 批量删除套餐信息
     *
     * @param id
     */
    void delSetMealById(@Param("id") Long id);

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    SetMeal querySetMealById(@Param("id") Long id);
}
