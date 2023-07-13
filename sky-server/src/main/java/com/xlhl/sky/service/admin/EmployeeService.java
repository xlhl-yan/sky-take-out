package com.xlhl.sky.service.admin;

import com.xlhl.sky.dto.EmployeeDTO;
import com.xlhl.sky.dto.EmployeeLoginDTO;
import com.xlhl.sky.dto.EmployeePageQueryDTO;
import com.xlhl.sky.entity.Employee;
import com.xlhl.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     *
     * @param employee
     */
    void save(EmployeeDTO employee) throws Exception;

    /**
     * 分页查询员工信息
     *
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     *
     * @param status 修改后状态码
     * @param id     员工id
     */
    void startOfStop(Integer status, Long id) throws Exception;

    /**
     * 根据id查询指定员工
     *
     * @param id 员工id
     * @return
     */
    Employee queryEmployeeById(Long id);

    /**
     * 根据id修改员工信息
     *
     * @param employeeDTO
     */
    void updateEmployee(EmployeeDTO employeeDTO) throws Exception;
}
