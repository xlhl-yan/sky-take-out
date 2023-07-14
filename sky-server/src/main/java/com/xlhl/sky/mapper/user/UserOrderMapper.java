package com.xlhl.sky.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlhl.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserOrderMapper extends BaseMapper<Orders> {

}
