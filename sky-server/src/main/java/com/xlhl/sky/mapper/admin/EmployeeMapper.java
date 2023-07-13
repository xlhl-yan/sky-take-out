package com.xlhl.sky.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.xlhl.sky.annotation.AutoFill;
import com.xlhl.sky.dto.EmployeePageQueryDTO;
import com.xlhl.sky.entity.Employee;
import com.xlhl.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工信息
     */
    @AutoFill(value = OperationType.INSERT)
    Integer insertEmployee(Employee employee);


    @AutoFill(value = OperationType.UPDATE)
    Integer updateEmployee(Employee employee);

    /**
     * 分页查询员工信息
     *
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQueryLikeName(EmployeePageQueryDTO employeePageQueryDTO);
}
