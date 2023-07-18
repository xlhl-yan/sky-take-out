package com.xlhl.sky.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.xlhl.sky.dto.GoodsSalesDTO;
import com.xlhl.sky.dto.OrdersPageQueryDTO;
import com.xlhl.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * 查询指定时间段的销量排名top10
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 销量top10
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
