package com.xlhl.sky.controller.user;

import com.xlhl.sky.dto.*;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.user.UserOrderService;
import com.xlhl.sky.vo.OrderPaymentVO;
import com.xlhl.sky.vo.OrderSubmitVO;
import com.xlhl.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户端订单相关接口")
public class UserOrderController {

    @Resource(name = "userOrderServiceImpl")
    private UserOrderService orderService;


    /**
     * 分页查询所有订单信息
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @ApiOperation(value = "查询历史订单信息")
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("正在查询历史订单。。。");

        PageResult pageResult = orderService.historyOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 再来一单
     * /user/order/repetition/{id}
     *
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation(value = "再来一单")
    public Result repetition(@PathVariable("id") Long orderId) {
        log.info("再来一单！==>{}", orderId);

        orderService.repetition(orderId);
        return Result.success();
    }

    /**
     * 根据id查询订单信息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation(value = "查看订单详细信息")
    public Result<OrderVO> orderDetail(@PathVariable("id") Long orderId) {
        log.info("查看订单详情==>{}", orderId);

        OrderVO orderVO = orderService.orderDetail(orderId);
        return Result.success(orderVO);
    }

    /**
     * 用户取消订单
     *
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation(value = "用户取消订单")
    public Result cancelOrder(@PathVariable("id") OrdersCancelDTO ordersCancelDTO) {
        log.info("用户取消订单==>{}", ordersCancelDTO);

        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }


    /**
     * 用户催单
     *
     * @param orderId
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation(value = "用户催单")
    public Result reminder(@PathVariable("id") Long orderId) {
        log.info("客户已催单==>{}，请尽快接单", orderId);

        orderService.reminder(orderId);
        return Result.success();
    }

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation(value = "用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单===>{}", ordersSubmitDTO);

        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }
}
