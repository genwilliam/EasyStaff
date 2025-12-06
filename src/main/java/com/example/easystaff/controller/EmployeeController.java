package com.example.easystaff.controller;

import com.example.easystaff.domain.Employee;
import com.example.easystaff.dto.ApiResponse;
import com.example.easystaff.dto.EmployeeQueryRequest;
import com.example.easystaff.dto.PageResult;
import com.example.easystaff.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:63344", allowCredentials = "true")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 分页 + 条件查询员工列表
     */
    @GetMapping
    public ApiResponse<PageResult<Employee>> list(@Valid EmployeeQueryRequest request) {
        PageResult<Employee> pageResult = employeeService.queryPage(request);
        return ApiResponse.success(pageResult);
    }

    /**
     * 新增员工
     */
    @PostMapping
    public ApiResponse<Void> add(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
        return ApiResponse.success("添加成功");
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ApiResponse.success("删除成功");
    }

    /**
     * 查看员工详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Employee> detail(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return ApiResponse.error("员工不存在");
        }
        return ApiResponse.success(employee);
    }
}


