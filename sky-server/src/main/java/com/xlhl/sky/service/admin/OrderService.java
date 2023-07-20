package com.xlhl.sky.service.admin;

import com.xlhl.sky.dto.OrdersCancelDTO;
import com.xlhl.sky.dto.OrdersPageQueryDTO;
import com.xlhl.sky.dto.OrdersRejectionDTO;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.vo.OrderStatisticsVO;
import com.xlhl.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     * @return
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 查看各个状态的订单数量
     *
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 完成订单
     *
     * @param id
     * @return
     */
    void complete(Long id);

    /**
     * 拒单
     *
     * @param rejectionDTO
     * @return
     */
    void rejection(OrdersRejectionDTO rejectionDTO);

    /**
     * 管理端接单
     *
     * @param id
     * @return
     */
    void confirm(Integer id);


    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    OrderVO details(Integer id);

    /**
     * 派送
     *
     * @param id
     * @return
     */
    void delivery(Integer id);

    /**
     * 搜索订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
}
