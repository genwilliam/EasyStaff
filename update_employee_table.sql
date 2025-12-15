-- 为现有数据库添加新字段的更新脚本
-- 如果数据库已经存在，执行此脚本来添加新字段

USE easystaff;

-- 添加新字段（如果字段不存在）
ALTER TABLE `employee` 
ADD COLUMN IF NOT EXISTS `employment_status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '在职状态' AFTER `position`,
ADD COLUMN IF NOT EXISTS `department` VARCHAR(50) DEFAULT NULL COMMENT '部门' AFTER `employment_status`,
ADD COLUMN IF NOT EXISTS `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱' AFTER `department`,
ADD COLUMN IF NOT EXISTS `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话' AFTER `email`;
