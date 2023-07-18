package com.xlhl.sky.controller.admin;

import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.admin.ReportService;
import com.xlhl.sky.vo.OrderReportVO;
import com.xlhl.sky.vo.SalesTop10ReportVO;
import com.xlhl.sky.vo.TurnoverReportVO;
import com.xlhl.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 数据统计相关接口
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据统计相关接口")
public class ReportController {

    @Resource(name = "reportServiceImpl")
    private ReportService reportService;


    /**
     * 查询指定时间段的销量排名top10
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 销量top10
     */
    @GetMapping("/top10")
    @ApiOperation(value = "查询指定时间段的销量排名top10")
    public Result<SalesTop10ReportVO> top10(
            //时间格式转换
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        log.info("查询销量排名top10。。。");
        SalesTop10ReportVO salesTop10ReportVO = reportService.getSalesTop10(begin, end);

        return Result.success(salesTop10ReportVO);
    }

    /**
     * 指定时间订单统计
     * /admin/report/ordersStatistics
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation(value = "订单统计")
    public Result<OrderReportVO> ordersStatistics(
            //时间格式转换
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("正在查询订单信息 {}===> {}", begin, end);

        OrderReportVO orderReportVO = reportService.getOrdersStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 指定时间用户统计
     * /admin/report/userStatistics
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation(value = "用户数据统计")
    public Result<UserReportVO> userStatistics(
            //时间格式转换
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("正在查询用户信息 {}===> {}", begin, end);

        UserReportVO userReportVO = reportService.getUserStatistics(begin, end);
        return Result.success(userReportVO);
    }

    /**
     * 指定时间营业额统计
     * /admin/report/admin/report
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation(value = "营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            //时间格式转换
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("正在查询营业额 {}===> {}", begin, end);

        TurnoverReportVO turnoverStatistics = reportService.getTurnoverStatistics(begin, end);
        return Result.success(turnoverStatistics);
    }
}
