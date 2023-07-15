package com.xlhl.sky.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.constant.StatusConstant;
import com.xlhl.sky.dto.SetMealDTO;
import com.xlhl.sky.dto.SetMealPageQueryDTO;
import com.xlhl.sky.entity.Dish;
import com.xlhl.sky.entity.SetMeal;
import com.xlhl.sky.entity.SetMealDish;
import com.xlhl.sky.exception.SetMealEnableFailedException;
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

        //==>新增套餐是否存在？
        SetMeal selectOne = setMealMapper.selectOne(new QueryWrapper<SetMeal>().eq("name", setMealDTO.getName()));
        assert selectOne == null;

        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);

        //插入套餐与菜品关联信息
        int addSetMeal = setMealMapper.insert(setMeal);

        assert addSetMeal == 1;

        List<SetMealDish> setMealDishes = setMealDTO.getSetmealDishes();

        Long setMealId = setMeal.getId();
        //插入套餐id
        setMealDishes.forEach(setMealDish -> {
            setMealDish.setSetmealId(setMealId);
        });
        //==>新增套餐关联的菜品信息
        Integer addSetMealDish = setMealDishMapper.addSetMealDish(setMealDishes);
        assert addSetMealDish >= 1;
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

        log.info("套餐数据为==>{}", page.getResult());
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
        assert status != null && id != null;
        if (status.equals(StatusConstant.ENABLE)) {//==>起售套餐内是否存在停售菜品？
            List<SetMealDish> setMealDishById = setMealDishMapper.getSetMealDishById(id);
            setMealDishById.forEach(setMealDish -> {
                Dish dish = dishMapper.queryDishById(setMealDish.getDishId());
                if (dish.getStatus().equals(StatusConstant.DISABLE)) {//===>存在未起售套餐则抛出异常
                    throw new SetMealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }
        SetMeal setMeal = SetMeal.builder()
                .id(id)
                .status(status)
                .build();

        setMealMapper.updateById(setMeal);
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
            //==>删除套餐信息
            setMealMapper.delSetMealById(id);

            //==>删除套餐关联菜品的信息
            setMealDishMapper.delSetMealDishBySetMealId(id);
        });


    }

    /**
     * 根据套餐id查询套餐信息
     *
     * @param setMealId
     * @return
     */
    @Override
    public SetMealVO querySetMealById(Long setMealId) {

//        SetMeal setmeal = setMealMapper.selectById(setMealId);
//        List<SetMealDish> setMealDishById = setMealDishMapper.getSetMealDishById(setMealId);
//
//        SetMealVO setmealVO = new SetMealVO();
//        BeanUtils.copyProperties(setmeal, setmealVO);
//        setmealVO.setSetMealDishes(setMealDishById);
        assert setMealId != null;
        //==>查询套餐信息
        SetMeal setMeal = setMealMapper.selectById(setMealId);
//        SetMeal setMeal = setMealMapper.querySetMealById(setMealId);

        assert setMeal != null;

        SetMealVO setMealVO = new SetMealVO();
        BeanUtils.copyProperties(setMeal, setMealVO);

        //==>查询套餐关联的菜品信息
        List<SetMealDish> setMealDishList = setMealDishMapper.getSetMealDishById(setMealId);
        setMealVO.setSetMealDishes(setMealDishList);
        return setMealVO;
    }
}
