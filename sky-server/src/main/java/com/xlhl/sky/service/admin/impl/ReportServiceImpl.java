package com.xlhl.sky.service.admin.impl;

import com.xlhl.sky.entity.Orders;
import com.xlhl.sky.mapper.user.UserOrderMapper;
import com.xlhl.sky.service.admin.ReportService;
import com.xlhl.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Resource
    private UserOrderMapper orderMapper;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //存放 begin——>end每天的日期
        List<LocalDate> dateTimes = new ArrayList<>();


        int i = 1;
        dateTimes.add(begin);
        while (begin.isBefore(end)) {//计算指定日期后一天日期
            dateTimes.add(begin = begin.plusDays(i));
        }

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
}
