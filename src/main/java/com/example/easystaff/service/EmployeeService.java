package com.example.easystaff.service;

import com.example.easystaff.domain.Employee;
import com.example.easystaff.dto.EmployeeQueryRequest;
import com.example.easystaff.dto.PageResult;

public interface EmployeeService {

    /**
     * 分页 + 条件查询员工列表
     */
    PageResult<Employee> queryPage(EmployeeQueryRequest request);

    /**
     * 新增员工
     */
    void addEmployee(Employee employee);

    /**
     * 删除员工
     */
    void deleteEmployee(Long id);

    /**
     * 查询员工详情
     */
    Employee getEmployeeById(Long id);
}


