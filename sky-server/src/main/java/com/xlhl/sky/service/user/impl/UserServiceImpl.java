package com.xlhl.sky.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.dto.UserLoginDTO;
import com.xlhl.sky.entity.User;
import com.xlhl.sky.exception.LoginFailedException;
import com.xlhl.sky.mapper.user.UserMapper;
import com.xlhl.sky.properties.WeChatProperties;
import com.xlhl.sky.service.user.UserService;
import com.xlhl.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Resource(name = "weChatProperties")
    private WeChatProperties weChatProperties;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User weChatLogin(UserLoginDTO userLoginDTO) {
        assert userLoginDTO != null;

        //==>调用微信接口服务 获取openid
        String openid = getOpenidByCode(userLoginDTO.getCode());
        //==>判断openid是否为空
        if (openid == null) {
            log.error("登录失败，");
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //==>当前用户是否为新用户
        User user = userMapper.queryUserByOpenid(openid);
        if (user == null) {
            log.info("新用户，准备为其注册");
            //==>自动注册
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insertUser(user);
        }

        //==>返回用户对象
        return user;
    }

    /**
     * 获取openid
     *
     * @param code
     * @return
     */
    private String getOpenidByCode(String code) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appid", weChatProperties.getAppid());
        hashMap.put("secret", weChatProperties.getSecret());
        hashMap.put("grant_type", "authorization_code");
        hashMap.put("js_code", code);
        String json = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", hashMap);
        return JSONObject.parseObject(json).getString("openid");
    }
}
