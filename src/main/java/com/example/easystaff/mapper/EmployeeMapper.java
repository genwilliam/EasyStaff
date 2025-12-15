package com.example.easystaff.mapper;

import com.example.easystaff.domain.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 统计符合条件的员工数量
     */
    long countByCondition(@Param("name") String name,
                          @Param("position") String position,
                          @Param("employmentStatus") String employmentStatus,
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate);

    /**
     * 分页查询符合条件的员工列表
     */
    List<Employee> findPageByCondition(@Param("name") String name,
                                       @Param("position") String position,
                                       @Param("employmentStatus") String employmentStatus,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    /**
     * 条件查询全量列表（用于导出）
     */
    List<Employee> findByCondition(@Param("name") String name,
                                   @Param("position") String position,
                                   @Param("employmentStatus") String employmentStatus,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    /**
     * 新增员工
     */
    int insert(Employee employee);

    /**
     * 根据主键删除
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据主键查询详情
     */
    Employee findById(@Param("id") Long id);

    /**
     * 更新员工信息
     */
    int update(Employee employee);

    /**
     * 批量删除员工
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 统计总员工数
     */
    long countTotal();

    /**
     * 按职位统计员工数量
     */
    List<java.util.Map<String, Object>> countByPosition();
}


