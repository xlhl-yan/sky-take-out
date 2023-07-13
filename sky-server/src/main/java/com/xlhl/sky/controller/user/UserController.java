package com.xlhl.sky.controller.user;


import com.xlhl.sky.constant.JwtClaimsConstant;
import com.xlhl.sky.dto.UserLoginDTO;
import com.xlhl.sky.entity.User;
import com.xlhl.sky.properties.JwtProperties;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.user.UserService;
import com.xlhl.sky.utils.JwtUtil;
import com.xlhl.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@Slf4j
@RestController
@Api(tags = "用户相关接口")
@RequestMapping("/user/user")
public class UserController {

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Resource(name = "jwtProperties")
    private JwtProperties jwtProperties;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "微信用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("code==>{}", userLoginDTO);
        //微信登录
        User user = userService.weChatLogin(userLoginDTO);

        //为微信用户生成jwt令牌
        HashMap<String, Object> map = new HashMap<>();
        map.put(JwtClaimsConstant.USER_ID, user.getId());

        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), map);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }
}
