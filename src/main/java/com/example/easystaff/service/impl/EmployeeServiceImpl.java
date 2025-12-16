package com.example.easystaff.service.impl;

import com.example.easystaff.domain.Employee;
import com.example.easystaff.dto.EmployeeQueryRequest;
import com.example.easystaff.dto.PageResult;
import com.example.easystaff.mapper.EmployeeMapper;
import com.example.easystaff.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @Override
    public PageResult<Employee> queryPage(EmployeeQueryRequest request) {
        int page = request.getPage() == null || request.getPage() < 1 ? 1 : request.getPage();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1 ? 4 : request.getPageSize();
        pageSize = Math.min(pageSize, 50); // 限制最大 50 条，防止一次拉取过多

        String name = request.getName();
        String position = request.getPosition();
        String employmentStatus = request.getEmploymentStatus();
        String department = request.getDepartment();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        long total = employeeMapper.countByCondition(name, position, employmentStatus, department, startDate, endDate);
        int offset = (page - 1) * pageSize;
        List<Employee> list = employeeMapper.findPageByCondition(name, position, employmentStatus, department, startDate, endDate, offset, pageSize);

        PageResult<Employee> pageResult = new PageResult<>();
        pageResult.setList(list);
        pageResult.setTotal(total);
        pageResult.setPage(page);
        pageResult.setPageSize(pageSize);
        return pageResult;
    }

    @Override
    public void addEmployee(Employee employee) {
        if (employee.getEmploymentStatus() == null || employee.getEmploymentStatus().isEmpty()) {
            employee.setEmploymentStatus("ACTIVE");
        }
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

    @Override
    public void updateEmployee(Employee employee) {
        if (employee.getEmploymentStatus() == null || employee.getEmploymentStatus().isEmpty()) {
            employee.setEmploymentStatus("ACTIVE");
        }
        employeeMapper.update(employee);
    }

    @Override
    public void deleteEmployees(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            employeeMapper.deleteByIds(ids);
        }
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总员工数
        long total = employeeMapper.countTotal();
        statistics.put("total", total);
        
        // 按职位统计
        List<Map<String, Object>> positionStats = employeeMapper.countByPosition();
        statistics.put("positionStats", positionStats);
        
        return statistics;
    }

    @Override
    public List<Employee> listForExport(EmployeeQueryRequest request) {
        String name = request.getName();
        String position = request.getPosition();
        String employmentStatus = request.getEmploymentStatus();
        String department = request.getDepartment();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        return employeeMapper.findByCondition(name, position, employmentStatus, department, startDate, endDate);
    }

    @Override
    public void batchUpdate(com.example.easystaff.dto.EmployeeBatchUpdateRequest request) {
        if (request == null || request.getIds() == null || request.getIds().isEmpty()) {
            return;
        }
        employeeMapper.batchUpdate(request.getIds(), request.getDepartment(), request.getPosition(), request.getEmploymentStatus(), request.getEntryDate());
    }

    @Override
    public int importEmployees(java.io.InputStream inputStream) throws java.io.IOException {
        java.util.List<Employee> buffer = new java.util.ArrayList<>();
        com.alibaba.excel.EasyExcel.read(inputStream, Employee.class, new com.alibaba.excel.read.listener.PageReadListener<Employee>(buffer::addAll)).sheet().doRead();
        int success = 0;
        for (Employee emp : buffer) {
            if (emp.getEmploymentStatus() == null || emp.getEmploymentStatus().isEmpty()) {
                emp.setEmploymentStatus("ACTIVE");
            }
            employeeMapper.insert(emp);
            success++;
        }
        return success;
    }
}


