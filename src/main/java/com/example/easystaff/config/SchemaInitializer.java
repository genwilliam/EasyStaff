package com.example.easystaff.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 简单的启动时表结构兜底，确保新增字段存在。
 * （若已手动执行 SQL 脚本，会自动跳过）
 */
@Component
public class SchemaInitializer {

    private static final Logger log = LoggerFactory.getLogger(SchemaInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    public SchemaInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void ensureEmployeeColumns() {
        try {
            jdbcTemplate.execute("""
                    ALTER TABLE employee
                    ADD COLUMN IF NOT EXISTS employment_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '在职状态' AFTER position,
                    ADD COLUMN IF NOT EXISTS department VARCHAR(50) DEFAULT NULL COMMENT '部门' AFTER employment_status,
                    ADD COLUMN IF NOT EXISTS email VARCHAR(100) DEFAULT NULL COMMENT '邮箱' AFTER department,
                    ADD COLUMN IF NOT EXISTS phone VARCHAR(20) DEFAULT NULL COMMENT '电话' AFTER email;
                    """);
            log.info("SchemaInitializer: employee 表字段检查完成");
        } catch (Exception e) {
            // 不影响服务启动，记录日志以便排查
            log.warn("SchemaInitializer: 自动添加字段失败，请手动执行 update_employee_table.sql", e);
        }
    }
}

