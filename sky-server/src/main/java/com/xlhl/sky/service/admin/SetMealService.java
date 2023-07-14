package com.xlhl.sky.service.admin;


import com.xlhl.sky.dto.SetMealDTO;
import com.xlhl.sky.dto.SetMealPageQueryDTO;
import com.xlhl.sky.entity.SetMeal;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.vo.DishItemVO;

import java.util.List;

public interface SetMealService {
    /**
     * 条件查询
     *
     * @param setMeal
     * @return
     */
    List<SetMeal> list(SetMeal setMeal);

    /**
     * 根据id查询菜品选项
     *
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

    /**
     * 新增套餐信息
     *
     * @param setMealDTO
     */
    void addSetMeal(SetMealDTO setMealDTO);

    /**
     * 修改套餐信息
     *
     * @param setMealDTO
     * @return
     */
    void updateSetMeal(SetMealDTO setMealDTO);

    /**
     * 分页查询套餐信息
     *
     * @return
     */
    PageResult page(SetMealPageQueryDTO setMealPageQueryDTO);

    /**
     * 修改套餐状态
     *
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    void delSetMealById(List<Long> ids);

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    SetMeal querySetMealById(Integer id);
}
