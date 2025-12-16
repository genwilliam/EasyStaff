package com.example.easystaff.mapper;

import com.example.easystaff.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     */
    User findByUsername(@Param("username") String username);

    /**
     * 新增用户
     */
    int insert(User user);

    /**
     * 分页查询用户
     */
    java.util.List<User> findPage(@Param("username") String username,
                                  @Param("role") String role,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    /**
     * 统计用户数量
     */
    long count(@Param("username") String username, @Param("role") String role);

    /**
     * 重置密码
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 更新角色
     */
    int updateRole(@Param("id") Long id, @Param("role") String role);
}


