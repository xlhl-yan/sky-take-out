package com.xlhl.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.constant.PasswordConstant;
import com.xlhl.sky.constant.StatusConstant;
import com.xlhl.sky.dto.EmployeeDTO;
import com.xlhl.sky.dto.EmployeeLoginDTO;
import com.xlhl.sky.dto.EmployeePageQueryDTO;
import com.xlhl.sky.entity.Employee;
import com.xlhl.sky.exception.AccountLockedException;
import com.xlhl.sky.exception.AccountNotFoundException;
import com.xlhl.sky.exception.PasswordErrorException;
import com.xlhl.sky.mapper.EmployeeMapper;
import com.xlhl.sky.result.PageResult;
import com.xlhl.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Resource(name = "employeeMapper")
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        //密码MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());


        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) throws Exception {
        log.info("当前线程的id==>{}", Thread.currentThread().getId());
        if (employeeDTO == null) {
            log.error(MessageConstant.EMPLOYEE_NULL);
            throw new Exception(MessageConstant.EMPLOYEE_NULL);
        }

        //拷贝数据
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账号状态    1:正常 0:锁定
        employee.setStatus(StatusConstant.ENABLE);

        //设置默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        /*
        //设置创建时间
        LocalDateTime dateTime = LocalDateTime.now();
        employee.setCreateTime(dateTime);
        employee.setUpdateTime(dateTime);

        //设置当前记录创建者id和修改者id
        //根据ThreadLocal获取当前用户id
        Long empId = BaseContext.getCurrentId();
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        */

        //插入新员工数据
        employeeMapper.insertEmployee(employee);
    }

    /**
     * 分页查询员工信息
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //参数校验
        if (employeePageQueryDTO.getPage() <= 0) {
            employeePageQueryDTO.setPage(1);
        }

        //开始分页查询插件
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQueryLikeName(employeePageQueryDTO);

        //封装数据
        long total = page.getTotal();
        List<Employee> result = page.getResult();

        return new PageResult(total, result);

    }

    /**
     * 启用禁用员工账号
     *
     * @param status 修改后状态码
     * @param id     员工id
     */
    @Override
    public void startOfStop(Integer status, Long id) throws Exception {
        if (!status.equals(StatusConstant.DISABLE) && !status.equals(StatusConstant.ENABLE)) {
            log.error("状态参数异常");
        }
        Employee build = Employee.builder()
                .status(status)
                .id(id)
                .build();
        int count = employeeMapper.updateById(build);
        if (count > 1) {
            throw new Exception(MessageConstant.UNKNOWN_ERROR);
        }
    }

    /**
     * 根据id查询指定员工
     *
     * @param id 员工id
     * @return
     */
    @Override
    public Employee queryEmployeeById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            log.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //将密码设置为******防止密码泄露
        employee.setPassword("******");
        return employee;
    }

    /**
     * 根据id修改员工信息
     *
     * @param employeeDTO
     */
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) throws Exception {
        if (employeeDTO.getId() == null) {
            log.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //数据拷贝
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        /*
        //设置更新时间与修改人员id
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        */

        int count = employeeMapper.updateEmployee(employee);
        if (count > 1) {
            log.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
