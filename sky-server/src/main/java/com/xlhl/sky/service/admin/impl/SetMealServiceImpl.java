package com.xlhl.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xlhl.sky.dto.SetMealDTO;
import com.xlhl.sky.dto.SetMealPageQueryDTO;
import com.xlhl.sky.entity.SetMeal;
import com.xlhl.sky.mapper.admin.DishMapper;
import com.xlhl.sky.mapper.admin.SetMealDishMapper;
import com.xlhl.sky.mapper.admin.SetMealMapper;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.service.admin.SetMealService;
import com.xlhl.sky.vo.DishItemVO;
import com.xlhl.sky.vo.SetMealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Resource(name = "setMealDishMapper")
    private SetMealDishMapper setMealDishMapper;

    @Resource(name = "dishMapper")
    private DishMapper dishMapper;

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
     *
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setMealMapper.getDishItemBySetMealId(id);
    }

    /**
     * 新增套餐信息
     *
     * @param setMealDTO
     */
    @Override
    public void addSetMeal(SetMealDTO setMealDTO) {
        assert setMealDTO != null;

        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);

        Integer integer = setMealMapper.addSetMeal(setMeal);
        assert integer == 1;
    }

    /**
     * 修改套餐信息
     *
     * @param setMealDTO
     * @return
     */
    @Override
    public void updateSetMeal(SetMealDTO setMealDTO) {
        assert setMealDTO != null;

        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);

        Integer integer = setMealMapper.updateSetMeal(setMeal);

        assert integer == 1;
    }

    /**
     * 分页查询套餐信息
     *
     * @param setMealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetMealPageQueryDTO setMealPageQueryDTO) {
        assert setMealPageQueryDTO != null;

        PageHelper.startPage(setMealPageQueryDTO.getPage(), setMealPageQueryDTO.getPageSize());
        Page<SetMealVO> page = setMealMapper.page(setMealPageQueryDTO);


        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改套餐状态
     *
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        assert status != null || id != null;

        setMealMapper.updateStatus(status, id);
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @Override
    public void delSetMealById(List<Long> ids) {
        assert ids.size() > 0;

        ids.forEach(id -> {
            setMealMapper.delSetMealById(id);
        });
    }

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    @Override
    public SetMeal querySetMealById(Integer id) {
        assert id != null;
        SetMeal setMeal = setMealMapper.querySetMealById(id);

        assert setMeal != null;
        return setMeal;
    }
}
