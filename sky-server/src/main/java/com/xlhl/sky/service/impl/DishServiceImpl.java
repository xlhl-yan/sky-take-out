package com.xlhl.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.constant.StatusConstant;
import com.xlhl.sky.dto.DishDTO;
import com.xlhl.sky.dto.DishPageQueryDTO;
import com.xlhl.sky.entity.Dish;
import com.xlhl.sky.entity.DishFlavor;
import com.xlhl.sky.exception.DeletionNotAllowedException;
import com.xlhl.sky.mapper.DishFlavorsMapper;
import com.xlhl.sky.mapper.DishMapper;
import com.xlhl.sky.mapper.SetMealDishMapper;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.DishService;
import com.xlhl.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
@Transactional
public class DishServiceImpl implements DishService {
    @Resource(name = "dishMapper")
    private DishMapper dishMapper;

    @Resource(name = "dishFlavorsMapper")
    private DishFlavorsMapper dishFlavorsMapper;

    @Resource(name = "setMealDishMapper")
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增菜品与口味数据
     *
     * @param dishDTO
     */
    @Override
    public Result addWithFlavor(DishDTO dishDTO) {
        //新增菜品数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        Dish selectOne = dishMapper.selectOne(new QueryWrapper<Dish>().eq("name", dishDTO.getName()));
        if (selectOne != null) {
            return Result.error(selectOne.getName() + "该菜品已存在!");
        }
        //新增
        dishMapper.addWith(dish);
        //获取insert语句生成的id
        Long dishId = dish.getId();

        //新增菜品口味数据
        List<DishFlavor> flavorsList = dishDTO.getFlavors();
        if (flavorsList.size() > 0) {
            //菜品id绑定口味数据中
            flavorsList.forEach(flavors -> {
                flavors.setDishId(dishId);
            });
            //插入口味数据
            dishFlavorsMapper.addFlavors(flavorsList);
        }
        return Result.success("菜品添加成功");
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //分页插件使用 分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(dishPage.getTotal(), dishPage.getResult());
    }

    /**
     * 根据id删除一个或多个菜品
     *
     * @param ids
     */
    @Override
    public void deleteDishByIds(List<Long> ids) {
        /*
         Dish(菜品表) DishFlavor(口味表) SetMealDish(套餐表) 涉及三
         可以删除多个或者一个菜品
         起售菜品无法删除
         被套餐关联的菜品无法删除
         删除菜品后关联的口味数据也应该删除
        */

        //==>是否存在起售中的菜品
        ids.forEach(id -> {
            Dish dish = dishMapper.selectById(id);
            if (dish.getStatus().equals(StatusConstant.ENABLE)) {
                log.error("{}:{} 菜品起售中，无法删除", id, dish.getName());
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });

        //==>是否存在套餐关联
        List<Long> idsList = setMealDishMapper.getSetMealIdsByIds(ids);
        if (idsList.size() > 0) {
            log.error("{}:菜品关联套餐，无法删除", idsList);
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        ids.forEach(dishId -> {
            //==>删除菜品表中的菜品数据
            //删除指定id菜品
            dishMapper.delById(dishId);

            //==>删除口味数据
            //删除指定id菜品关联口味
            dishFlavorsMapper.delById(dishId);
        });

    }
}
