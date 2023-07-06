package com.xlhl.sky.service;

import com.xlhl.sky.dto.EmployeeLoginDTO;
import com.xlhl.sky.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
