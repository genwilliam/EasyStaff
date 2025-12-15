-- 批量插入示例员工（20+条）
USE easystaff;

INSERT INTO `employee`
(`name`, `age`, `position`, `employment_status`, `department`, `email`, `phone`, `entry_date`, `create_time`, `update_time`)
VALUES
('张三', 28, '开发工程师', 'ACTIVE', '研发部', 'zhangsan@example.com', '13800010001', '2024-01-05', NOW(), NOW()),
('李四', 32, '测试工程师', 'ACTIVE', '质量部', 'lisi@example.com', '13800010002', '2024-02-10', NOW(), NOW()),
('王五', 26, '产品经理', 'ACTIVE', '产品部', 'wangwu@example.com', '13800010003', '2024-03-15', NOW(), NOW()),
('赵六', 30, '开发工程师', 'ACTIVE', '研发部', 'zhaoliu@example.com', '13800010004', '2024-04-01', NOW(), NOW()),
('钱七', 29, '运维工程师', 'ACTIVE', '运维部', 'qianqi@example.com', '13800010005', '2024-05-20', NOW(), NOW()),
('孙八', 35, '架构师', 'ACTIVE', '架构组', 'sunba@example.com', '13800010006', '2024-06-18', NOW(), NOW()),
('周九', 24, '实习生', 'ACTIVE', '研发部', 'zhoujiu@example.com', '13800010007', '2024-07-22', NOW(), NOW()),
('吴十', 27, '前端工程师', 'ACTIVE', '研发部', 'wushi@example.com', '13800010008', '2024-08-05', NOW(), NOW()),
('郑十一', 31, '后端工程师', 'ACTIVE', '研发部', 'zhengshiyi@example.com', '13800010009', '2024-08-12', NOW(), NOW()),
('王十二', 33, '测试开发', 'ACTIVE', '质量部', 'wangshier@example.com', '13800010010', '2024-08-20', NOW(), NOW()),
('冯十三', 29, '数据工程师', 'ACTIVE', '数据部', 'fengshisan@example.com', '13800010011', '2024-09-01', NOW(), NOW()),
('陈十四', 34, '产品运营', 'ACTIVE', '产品部', 'chenshisi@example.com', '13800010012', '2024-09-10', NOW(), NOW()),
('褚十五', 28, 'UI设计师', 'ACTIVE', '设计部', 'chushiwuw@example.com', '13800010013', '2024-09-18', NOW(), NOW()),
('卫十六', 30, 'UX设计师', 'ACTIVE', '设计部', 'weishiliu@example.com', '13800010014', '2024-09-25', NOW(), NOW()),
('蒋十七', 27, '实施工程师', 'ACTIVE', '交付部', 'jiangshiqi@example.com', '13800010015', '2024-10-02', NOW(), NOW()),
('沈十八', 36, '项目经理', 'ACTIVE', '项目部', 'shenshiba@example.com', '13800010016', '2024-10-10', NOW(), NOW()),
('韩十九', 25, '售前工程师', 'ACTIVE', '售前部', 'hanshijiu@example.com', '13800010017', '2024-10-18', NOW(), NOW()),
('杨二十', 29, '售后工程师', 'ACTIVE', '售后部', 'yangershi@example.com', '13800010018', '2024-10-25', NOW(), NOW()),
('朱二一', 31, '安全工程师', 'ACTIVE', '安全部', 'zhueriyi@example.com', '13800010019', '2024-11-01', NOW(), NOW()),
('秦二二', 28, '运维工程师', 'ACTIVE', '运维部', 'qinerrer@example.com', '13800010020', '2024-11-08', NOW(), NOW()),
('尤二三', 27, '数据分析师', 'ACTIVE', '数据部', 'youerisan@example.com', '13800010021', '2024-11-15', NOW(), NOW()),
('许二四', 26, '算法工程师', 'ACTIVE', '算法部', 'xuerisi@example.com', '13800010022', '2024-11-22', NOW(), NOW()),
('何二五', 33, '运维工程师', 'ACTIVE', '运维部', 'heerwu@example.com', '13800010023', '2024-11-29', NOW(), NOW()),
('吕二六', 32, 'DBA', 'ACTIVE', '数据部', 'lverliu@example.com', '13800010024', '2024-12-06', NOW(), NOW()),
('施二七', 29, '测试工程师', 'ACTIVE', '质量部', 'shierqi@example.com', '13800010025', '2024-12-13', NOW(), NOW());
