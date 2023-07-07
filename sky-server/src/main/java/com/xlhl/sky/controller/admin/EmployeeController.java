package com.xlhl.sky.controller.admin;

import com.xlhl.sky.constant.JwtClaimsConstant;
import com.xlhl.sky.dto.EmployeeDTO;
import com.xlhl.sky.dto.EmployeeLoginDTO;
import com.xlhl.sky.entity.Employee;
import com.xlhl.sky.properties.JwtProperties;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.service.EmployeeService;
import com.xlhl.sky.utils.JwtUtil;
import com.xlhl.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录功能")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result<String> save(@RequestBody EmployeeDTO employee) throws Exception {
        log.info("当前线程的id==>{}", Thread.currentThread().getId());
        log.info("新增员工:{}", employee);
        employeeService.save(employee);
        return Result.success();
    }
}
