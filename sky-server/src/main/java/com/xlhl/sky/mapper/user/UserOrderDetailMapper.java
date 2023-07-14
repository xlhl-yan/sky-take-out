package com.xlhl.sky.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlhl.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserOrderDetailMapper extends BaseMapper<OrderDetail> {
    /**
     * 批量插入订单信息数据
     *
     * @param orderDetailList
     * @return
     */
    Integer insertBatch(List<OrderDetail> orderDetailList);
}
