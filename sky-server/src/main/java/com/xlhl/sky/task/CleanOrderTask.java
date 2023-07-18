package com.xlhl.sky.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlhl.sky.entity.Orders;
import com.xlhl.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class CleanOrderTask {

    @Resource
    private OrderMapper userOrderMapper;


    private QueryWrapper<Orders> wrapper = new QueryWrapper<>();
/*
    //===>指定多个时间执行任务
    @Schedules({
            @Scheduled(cron = "0 0 0 * * * "),
            @Scheduled(cron = "0 0 0 * * * "),
            @Scheduled(cron = "0 0 0 * * * "),
            @Scheduled(cron = "0 0 0 * * * ")
    })
*/

    /**
     * 每天 04:00 判断全部订单状态是否完成
     */
    @Scheduled(cron = "0 * * * * ?")
    public void cleanTimeoutOrders() {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            wrapper.eq("status", Orders.PENDING_PAYMENT);
            log.info("执行定时任务时间===>{}", dateTime);

            //查询是否存在超时订单==>代付款状态
            wrapper.le("order_time", dateTime.minusMinutes(15));
            wrapper.eq("status", Orders.PENDING_PAYMENT);
            List<Orders> ordersList = userOrderMapper.selectList(wrapper);

            if (ordersList.size() >= 1) {
                //修改订单状态
                ordersList.forEach(orders -> {
                    orders.setStatus(Orders.CANCELLED);
                    orders.setConsignee("订单超时自动取消订单");
                    orders.setOrderTime(dateTime);

                    log.info("当前订单信息为==>{}", orders);
                    userOrderMapper.updateById(orders);
                });
            }
            cleanLongTimeOrder();
        } finally {
            wrapper.clear();
        }


    }

    /**
     * 处理长时间派送中的订单
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void cleanLongTimeOrder() {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            log.info("定时处理长时间派送中订单==>{}", dateTime);

            //订单长时间派送中订单
            wrapper.le("order_time", dateTime.minusHours(4));
            wrapper.eq("status", Orders.COMPLETED);
            List<Orders> ordersList = userOrderMapper.selectList(wrapper);
            if (ordersList.size() >= 1) {
                //修改订单状态
                ordersList.forEach(orders -> {
                    orders.setStatus(Orders.COMPLETED);

                    userOrderMapper.updateById(orders);
                });
            }
        } finally {
            wrapper.clear();
        }
    }
}
