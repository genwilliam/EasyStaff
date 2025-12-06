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
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate);

    /**
     * 分页查询符合条件的员工列表
     */
    List<Employee> findPageByCondition(@Param("name") String name,
                                       @Param("position") String position,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

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
}


