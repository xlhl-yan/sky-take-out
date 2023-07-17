package com.xlhl.sky.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.xlhl.sky.dto.OrdersPageQueryDTO;
import com.xlhl.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface UserOrderMapper extends BaseMapper<Orders> {

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 分页查询历史订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> list(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据动态条件统计营业额
     *
     * @param map
     * @return
     */
    Double sumByMap(Map<String, Object> map);

}
