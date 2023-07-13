package com.xlhl.sky.service.user;

import com.xlhl.sky.dto.UserLoginDTO;
import com.xlhl.sky.entity.User;

public interface UserService {
    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    User weChatLogin(UserLoginDTO userLoginDTO);
}
