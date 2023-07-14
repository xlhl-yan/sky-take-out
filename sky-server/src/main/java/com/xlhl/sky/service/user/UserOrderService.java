package com.xlhl.sky.service.user;


import com.xlhl.sky.dto.OrdersSubmitDTO;
import com.xlhl.sky.vo.OrderSubmitVO;

public interface UserOrderService {
    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
