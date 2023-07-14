package com.xlhl.sky.controller.user;

import com.xlhl.sky.dto.OrdersSubmitDTO;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.user.UserOrderService;
import com.xlhl.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户端订单相关接口")
public class UserOrderController {

    @Resource(name = "userOrderServiceImpl")
    private UserOrderService userOrderService;

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

        OrderSubmitVO orderSubmitVO = userOrderService.submitOrder(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }
}
