package com.xlhl.sky.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.dto.OrdersCancelDTO;
import com.xlhl.sky.dto.OrdersPageQueryDTO;
import com.xlhl.sky.dto.OrdersRejectionDTO;
import com.xlhl.sky.entity.OrderDetail;
import com.xlhl.sky.entity.Orders;
import com.xlhl.sky.exception.OrderBusinessException;
import com.xlhl.sky.mapper.OrderDetailMapper;
import com.xlhl.sky.mapper.OrderMapper;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.service.admin.OrderService;
import com.xlhl.sky.vo.OrderStatisticsVO;
import com.xlhl.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     * @return
     */
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        assert ordersCancelDTO.getId() != null;


        Orders order = Orders.builder()
                .id(ordersCancelDTO.getId())
                .cancelReason(ordersCancelDTO.getCancelReason())
                .cancelTime(LocalDateTime.now())
                .status(Orders.CANCELLED)
                .build();

        //派送与完成状态的订单不可取消
        Orders orders = orderMapper.selectById(order.getId());
        Integer status = orders.getStatus();
        if (status.equals(Orders.DELIVERY_IN_PROGRESS) || status.equals(Orders.COMPLETED)) {
            throw new OrderBusinessException("派送中或已完成的订单不可取消");
        }

        //修改订单状态
        orderMapper.updateById(order);
    }

    /**
     * 查看各个状态的订单数量
     *
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();

        //待派送数量
        wrapper.eq("status", Orders.CONFIRMED);
        Long confirmed = orderMapper.selectCount(wrapper);

        //派送中数量
        wrapper.eq("status", Orders.DELIVERY_IN_PROGRESS);
        Long deliveryInProgress = orderMapper.selectCount(wrapper);
        //待接单数量
        wrapper.eq("status", Orders.CONFIRMED);
        Long toBeConfirmed = orderMapper.selectCount(wrapper);

        OrderStatisticsVO statisticsVO = new OrderStatisticsVO();
        statisticsVO.setConfirmed(confirmed.intValue());
        statisticsVO.setDeliveryInProgress(deliveryInProgress.intValue());
        statisticsVO.setToBeConfirmed(toBeConfirmed.intValue());

        return statisticsVO;
    }

    /**
     * 完成订单
     *
     * @param id
     * @return
     */
    @Override
    public void complete(Long id) {
        Orders order = verificationOrderById(id.intValue());

        if (!order.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //修改状态
        order.setStatus(Orders.COMPLETED);
        //记录送达时间
        order.setDeliveryTime(LocalDateTime.now());

        orderMapper.updateById(order);
    }

    /**
     * 拒单
     *
     * @param rejectionDTO
     * @return
     */
    @Override
    public void rejection(OrdersRejectionDTO rejectionDTO) {
        assert rejectionDTO.getId() != null;

        //查询订单
        Orders order = orderMapper.selectById(rejectionDTO.getId());

        //待接单状态方可拒单
        if (!order.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //拒单原因
        order.setRejectionReason(rejectionDTO.getRejectionReason());
        //修改其状态
        order.setStatus(Orders.CANCELLED);
        //拒单时间
        order.setCancelTime(LocalDateTime.now());

        orderMapper.updateById(order);
    }

    /**
     * 管理端接单
     *
     * @param id
     * @return
     */
    @Override
    public void confirm(Integer id) {
        assert id != null;

        //查询订单
        Orders order = orderMapper.selectById(id);

        //待接单状态方可接单
        if (!order.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        order.setStatus(Orders.CONFIRMED);

        orderMapper.updateById(order);
    }


    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO details(Integer id) {
        Orders orders = verificationOrderById(id);

        //查询订单详情信息
        List<OrderDetail> detailList = orderDetailMapper.selectList(
                new QueryWrapper<OrderDetail>().eq("order_id", orders.getId())
        );


        //数据封装
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(detailList);
        return orderVO;
    }

    /**
     * 派送
     *
     * @param id
     * @return
     */
    @Override
    public void delivery(Integer id) {
        Orders orders = verificationOrderById(id);

        if (!orders.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //改变状态
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        //设置预计送达时间
        orders.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(1));

        orderMapper.updateById(orders);
    }

    /**
     * 搜索订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        assert ordersPageQueryDTO != null;

        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> orders = orderMapper.queryPageOrder(ordersPageQueryDTO);

        return new PageResult(orders.getTotal(), orders.getResult());
    }

    /**
     * 判断订单是否存在
     *
     * @param id 订单id
     * @return 订单详情
     */
    private Orders verificationOrderById(Integer id) {
        assert id != null;

        //查询订单信息
        Orders orders = orderMapper.selectById(id);

        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        return orders;
    }
}
