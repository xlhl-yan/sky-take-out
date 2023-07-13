package com.xlhl.sky.service.admin;


import com.xlhl.sky.entity.SetMeal;
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
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
