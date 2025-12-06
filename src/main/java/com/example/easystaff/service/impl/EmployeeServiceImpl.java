package com.example.easystaff.service.impl;

import com.example.easystaff.domain.Employee;
import com.example.easystaff.dto.EmployeeQueryRequest;
import com.example.easystaff.dto.PageResult;
import com.example.easystaff.mapper.EmployeeMapper;
import com.example.easystaff.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @Override
    public PageResult<Employee> queryPage(EmployeeQueryRequest request) {
        int page = request.getPage() == null || request.getPage() < 1 ? 1 : request.getPage();
        int pageSize = 4; // 需求：每页固定 4 条

        String name = request.getName();
        String position = request.getPosition();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        long total = employeeMapper.countByCondition(name, position, startDate, endDate);
        int offset = (page - 1) * pageSize;
        List<Employee> list = employeeMapper.findPageByCondition(name, position, startDate, endDate, offset, pageSize);

        PageResult<Employee> pageResult = new PageResult<>();
        pageResult.setList(list);
        pageResult.setTotal(total);
        pageResult.setPage(page);
        pageResult.setPageSize(pageSize);
        return pageResult;
    }

    @Override
    public void addEmployee(Employee employee) {
        employeeMapper.insert(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeMapper.deleteById(id);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeMapper.findById(id);
    }
}


