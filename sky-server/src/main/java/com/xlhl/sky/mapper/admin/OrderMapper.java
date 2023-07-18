package com.xlhl.sky.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlhl.sky.entity.Orders;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface OrderMapper extends BaseMapper<Orders> {

    /**
     * 指定时间段订单总数
     *
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 指定时间段订单销售总额
     *
     * @param map
     * @return
     */
    Double sumByMap(Map map);
}
