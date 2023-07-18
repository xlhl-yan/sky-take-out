package com.xlhl.sky.service.admin;


import com.xlhl.sky.vo.OrderReportVO;
import com.xlhl.sky.vo.SalesTop10ReportVO;
import com.xlhl.sky.vo.TurnoverReportVO;
import com.xlhl.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * 指定时间营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 指定时间用户统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 指定时间订单统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return
     */
    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);

    /**
     * 查询指定时间段的销量排名top10
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 销量top10
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
