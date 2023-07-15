package com.xlhl.sky.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlhl.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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

//    /**
//     * 修改订单信息
//     *
//     * @param orders
//     */
//    void update(Orders orders);
//
//    /**
//     * 根据id修改订单信息
//     *
//     * @param orders
//     */
//    void updateOrders(Orders orders);
}
