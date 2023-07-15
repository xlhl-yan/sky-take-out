package com.xlhl.sky.mapper.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlhl.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据openId查询用户信息
     *
     * @param openid
     * @return
     */
    User queryUserByOpenid(@Param("openid") String openid);

    /**
     * 新增用户信息
     *
     * @param user
     * @return
     */
    Integer insertUser(User user);
}
