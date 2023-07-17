package com.xlhl.sky.service.user;


import com.xlhl.sky.dto.*;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.vo.OrderPaymentVO;
import com.xlhl.sky.vo.OrderSubmitVO;
import com.xlhl.sky.vo.OrderVO;

public interface UserOrderService {
    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 用户催单
     *
     * @param orderId
     */
    void reminder(Long orderId);

    /**
     * 用户取消订单
     *
     * @param ordersCancelDTO
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 根据id查询订单信息
     *
     * @param orderId
     * @return
     */
    OrderVO orderDetail(Long orderId);

    /**
     * 再来一单
     *
     * @return
     */
    void repetition(Long orderId);

    /**
     * 分页查询历史订单查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);
}
