package com.xlhl.sky.service.user;


import com.xlhl.sky.dto.OrdersPaymentDTO;
import com.xlhl.sky.dto.OrdersSubmitDTO;
import com.xlhl.sky.vo.OrderPaymentVO;
import com.xlhl.sky.vo.OrderSubmitVO;

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
}
