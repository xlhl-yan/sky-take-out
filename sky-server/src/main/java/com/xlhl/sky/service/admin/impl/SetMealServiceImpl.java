package com.xlhl.sky.service.admin.impl;

import com.xlhl.sky.entity.SetMeal;
import com.xlhl.sky.mapper.admin.SetMealMapper;
import com.xlhl.sky.service.admin.SetMealService;
import com.xlhl.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class SetMealServiceImpl implements SetMealService {
    @Resource(name = "setMealMapper")
    private SetMealMapper setMealMapper;

    /**
     * 条件查询
     *
     * @param setmeal
     * @return
     */
    @Override
    public List<SetMeal> list(SetMeal setmeal) {
        return setMealMapper.list(setmeal);
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setMealMapper.getDishItemBySetMealId(id);
    }
}
