package com.xlhl.sky.controller.admin;

import com.xlhl.sky.dto.OrdersCancelDTO;
import com.xlhl.sky.dto.OrdersPageQueryDTO;
import com.xlhl.sky.dto.OrdersRejectionDTO;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.admin.OrderService;
import com.xlhl.sky.vo.OrderStatisticsVO;
import com.xlhl.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/admin/order")
@Api(tags = "订单管理")
//TODO 管理端对订单操作具体实现
public class OrderController {
    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    /**
     * 搜索订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation(value = "搜索订单")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("搜索订单。。。{}", ordersPageQueryDTO);

        PageResult result = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 派送
     *
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation(value = "派送订单")
    public Result delivery(@PathVariable("id") Integer id) {
        log.info("订单正在派送中。。{}", id);

        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable("id") Integer id) {

        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 管理端接单
     *
     * @param id
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation(value = "管理端接单")
    public Result confirm(@RequestBody Integer id) {
        log.info("管理端正在接单。。。{}", id);

        orderService.confirm(id);
        return Result.success();
    }

    /**
     * 拒单
     *
     * @param rejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation(value = "管理端拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO rejectionDTO) {
        log.info("商家拒单。。。{},原因是{}", rejectionDTO.getId(), rejectionDTO.getRejectionReason());

        orderService.rejection(rejectionDTO);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param id
     * @return
     */
    @GetMapping("/complete/{id}")
    public Result complete(@PathVariable("id") Long id) {
        log.info("已完成订单{} 。。。", id);

        orderService.complete(id);
        return Result.success();
    }

    /**
     * 查看各个状态的订单数量
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "查看各个状态的订单数量")
    public Result<OrderStatisticsVO> statistics() {
        log.info("正在查询各个状态的订单数量。。。");

        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation(value = "店家取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("店家取消订单{},原因是{}", ordersCancelDTO.getId(), ordersCancelDTO.getCancelReason());

        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }
}
