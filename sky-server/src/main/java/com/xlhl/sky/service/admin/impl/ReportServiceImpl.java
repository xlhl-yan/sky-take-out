package com.xlhl.sky.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlhl.sky.dto.GoodsSalesDTO;
import com.xlhl.sky.entity.Orders;
import com.xlhl.sky.entity.User;
import com.xlhl.sky.mapper.UserMapper;
import com.xlhl.sky.mapper.OrderDetailMapper;
import com.xlhl.sky.mapper.OrderMapper;
import com.xlhl.sky.service.admin.ReportService;
import com.xlhl.sky.vo.OrderReportVO;
import com.xlhl.sky.vo.SalesTop10ReportVO;
import com.xlhl.sky.vo.TurnoverReportVO;
import com.xlhl.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;


    /**
     * 指定时间营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //动态获取开始——>结束所有日期
        List<LocalDate> dateTimes = getLocalDates(begin, end);

        log.info("查询此时间段营业额==>{}", dateTimes);

        List<Double> turnoverList = new ArrayList<>();
        dateTimes.forEach(localDate -> {
            //查询对应日期完成的订单金额合计
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);//一天的开始
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);//一天的结束

            //select sum(amount) from orders where
            //order_time > beginTime and order_time < endTime and status = 5

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByMap(map);

            turnoverList.add(turnover == null ? 0d : turnover);
        });


        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateTimes, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 指定时间用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //动态获取开始——>结束所有日期
        List<LocalDate> dateTimes = getLocalDates(begin, end);

        //存放用户总数
        List<Long> totalUsers = new ArrayList<>();
        //存放用户新增总数
        List<Long> newUsers = new ArrayList<>();

        //查询指定时间段用户的总量
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        dateTimes.forEach(localDate -> {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);//一天的开始
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);//一天的结束

            //当前时间之前注册 当天23.59之前创建
            //直至当天用户的总量
            wrapper.lt("create_time", endTime);
            Long totalUser = userMapper.selectCount(wrapper);

            //当天总用户的数量
            totalUsers.add(totalUser);

            //当天0：00 ——>23.59创建的用户
            wrapper.gt("create_time", beginTime);
            Long newUser = userMapper.selectCount(wrapper);

            //当天新增用户的数量
            newUsers.add(newUser);
            //清空条件处理器
            wrapper.clear();
        });

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateTimes, ","))
                .newUserList(StringUtils.join(newUsers, ","))
                .totalUserList(StringUtils.join(totalUsers, ","))
                .build();
    }

    /**
     * 指定时间订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        //动态获取开始——>结束所有日期
        List<LocalDate> dateTimes = getLocalDates(begin, end);


        //存放用户总数
        List<Long> totalOrders = new ArrayList<>();
        //存放用户新增总数
        List<Long> validOrders = new ArrayList<>();

        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        dateTimes.forEach(localDate -> {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);//一天的开始
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);//一天的结束

            //查询每天订单总数
            //select count(id) from orders where order_time < ? and order_time > ?
            wrapper.lt("order_time", endTime);
            wrapper.gt("order_time", beginTime);
            Long totalOrder = orderMapper.selectCount(wrapper);
            totalOrders.add(totalOrder);

            //查询每天的有效订单数
            //select count(id) from orders where order_time < ? and order_time > ? and status = 5
            wrapper.eq("status", Orders.COMPLETED);
            Long validOrder = orderMapper.selectCount(wrapper);
            validOrders.add(validOrder);

            wrapper.clear();
        });

        //计算时间区间全部的订单数量 lambda求和
        long totalOrderCount = totalOrders.stream().reduce(Long::sum).get();
        //计算时间区间全部的有效订单数量 lambda求和
        long validOrdersCount = validOrders.stream().reduce(Long::sum).get();

        double orderCompletionRate = 0d;
        if (totalOrderCount != 0L) {
            orderCompletionRate = (double) validOrdersCount / totalOrderCount;
        }
        //计算时间区间内全部的有效订单数量
        return OrderReportVO.builder()
                //每天订单总数
                .orderCountList(StringUtils.join(totalOrders, ","))
                //每天有效订单数
                .validOrderCountList(StringUtils.join(validOrders, ","))
                //时间段
                .dateList(StringUtils.join(dateTimes, ","))
                //时间段内全部订单
                .validOrderCount(Math.toIntExact(validOrdersCount))
                //时间段有效订单数量
                .totalOrderCount(Math.toIntExact(totalOrderCount))
                //订单完成效率
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 查询指定时间段的销量排名top10
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 销量top10
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {


        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);//当天的开始
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);//当天的结束
        /**
         * SELECT od.name,SUM(od.number) FROM orders o JOIN orders_detail od
         * WHERE od.order_id = o.id AND o.status = 5 AND o.order_time < ? AND o.order_time > ?
         * GROUP BY od.name ORDER BY number DESC LIMIT 0,10
         */


        List<GoodsSalesDTO> goodsSalesList = orderMapper.getSalesTop10(beginTime, endTime);


        List<String> nameList = goodsSalesList.stream()
                .map(GoodsSalesDTO::getName)
                .collect(Collectors.toList());

        List<Integer> numberList = goodsSalesList.stream()
                .map(GoodsSalesDTO::getNumber)
                .collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    /**
     * 动态获取 开始 ——> 结束 之间所有日期
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 动态获取的日期集合
     */
    @NotNull
    private List<LocalDate> getLocalDates(LocalDate begin, LocalDate end) {
        //存放 begin——>end每天的日期
        List<LocalDate> dateTimes = new ArrayList<>();

        int i = 1;
        dateTimes.add(begin);
        while (begin.isBefore(end)) {//计算指定日期后一天日期
            dateTimes.add(begin = begin.plusDays(i));
        }
        return dateTimes;
    }
}
