package com.example.easystaff.controller;

import com.example.easystaff.domain.Employee;
import com.example.easystaff.dto.ApiResponse;
import com.example.easystaff.dto.EmployeeBatchUpdateRequest;
import com.example.easystaff.dto.EmployeeQueryRequest;
import com.example.easystaff.dto.PageResult;
import com.example.easystaff.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResponse<Void> add(@RequestBody Employee employee, HttpSession session) {
        if (!isAdmin(session)) {
            return ApiResponse.error("仅管理员可新增");
        }
        employeeService.addEmployee(employee);
        return ApiResponse.success("添加成功");
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return ApiResponse.error("仅管理员可删除");
        }
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

    /**
     * 更新员工信息
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody Employee employee, HttpSession session) {
        if (!isAdmin(session)) {
            return ApiResponse.error("仅管理员可修改");
        }
        employee.setId(id);
        Employee existing = employeeService.getEmployeeById(id);
        if (existing == null) {
            return ApiResponse.error("员工不存在");
        }
        employeeService.updateEmployee(employee);
        return ApiResponse.success("更新成功");
    }

    /**
     * 批量删除员工
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDelete(@RequestBody List<Long> ids, HttpSession session) {
        if (!isAdmin(session)) {
            return ApiResponse.error("仅管理员可删除");
        }
        if (ids == null || ids.isEmpty()) {
            return ApiResponse.error("请选择要删除的员工");
        }
        employeeService.deleteEmployees(ids);
        return ApiResponse.success("批量删除成功");
    }

    /**
     * 批量更新员工
     */
    @PutMapping("/batch")
    public ApiResponse<Void> batchUpdate(@RequestBody EmployeeBatchUpdateRequest request, HttpSession session) {
        if (!isAdmin(session)) {
            return ApiResponse.error("仅管理员可修改");
        }
        if (request.getIds() == null || request.getIds().isEmpty()) {
            return ApiResponse.error("请选择要更新的员工");
        }
        employeeService.batchUpdate(request);
        return ApiResponse.success("批量更新成功");
    }

    /**
     * Excel 导入员工
     */
    @PostMapping("/import")
    public ApiResponse<String> importEmployees(@RequestPart("file") MultipartFile file, HttpSession session) {
        if (!isAdmin(session)) {
            return ApiResponse.error("仅管理员可导入");
        }
        if (file == null || file.isEmpty()) {
            return ApiResponse.error("请上传文件");
        }
        try {
            int success = employeeService.importEmployees(file.getInputStream());
            return ApiResponse.success("导入成功：" + success + " 条");
        } catch (IOException e) {
            return ApiResponse.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 获取员工统计信息
     */
    @GetMapping("/statistics")
    public ApiResponse<java.util.Map<String, Object>> statistics() {
        java.util.Map<String, Object> statistics = employeeService.getStatistics();
        return ApiResponse.success(statistics);
    }

    /**
     * 导出当前查询结果为 CSV
     */
    @GetMapping("/export")
    public void export(@Valid EmployeeQueryRequest request, HttpServletResponse response) throws IOException {
        List<Employee> employees = employeeService.listForExport(request);

        String filename = URLEncoder.encode("employees.csv", StandardCharsets.UTF_8);
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        String header = "ID,姓名,年龄,职位,状态,部门,邮箱,电话,入职日期,创建时间,更新时间\n";
        response.getWriter().write(header);
        for (Employee emp : employees) {
            response.getWriter().write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                    safe(emp.getId()),
                    safe(emp.getName()),
                    safe(emp.getAge()),
                    safe(emp.getPosition()),
                    safe(emp.getEmploymentStatus()),
                    safe(emp.getDepartment()),
                    safe(emp.getEmail()),
                    safe(emp.getPhone()),
                    safe(emp.getEntryDate()),
                    safe(emp.getCreateTime()),
                    safe(emp.getUpdateTime())));
        }
        response.getWriter().flush();
    }

    private String safe(Object obj) {
        if (obj == null) {
            return "";
        }
        return String.valueOf(obj).replace(",", " ");
    }

    private boolean isAdmin(HttpSession session) {
        Object obj = session != null ? session.getAttribute(UserController.SESSION_USER_KEY) : null;
        if (obj instanceof com.example.easystaff.domain.User user) {
            return "ADMIN".equalsIgnoreCase(user.getRole());
        }
        return false;
    }
}


