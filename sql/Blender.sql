SELECT VERSION() as MySQL版本,
       DATABASE() as 当前数据库,
       USER() as 当前用户,
       NOW() as 当前时间;

SHOW DATABASES;

USE blender;

SELECT DATABASE();

-- 1. 用户表（存储用户基本信息）
CREATE TABLE user (
                      user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一标识',
                      username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                      password VARCHAR(255) NOT NULL COMMENT '加密密码',
                      email VARCHAR(100) COMMENT '邮箱'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基本信息表';

-- 2. 账单来源类型表（微信、支付宝等）
CREATE TABLE bill_source_type (
                                  source_type_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '来源类型ID',
                                  source_name VARCHAR(50) NOT NULL UNIQUE COMMENT '来源名称',
                                  description VARCHAR(200) COMMENT '类型描述',
                                  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单来源类型表';

-- 3. 账单分类表（收支分类）
CREATE TABLE bill_category (
                               category_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
                               category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
                               parent_id INT DEFAULT 0 COMMENT '父级分类ID',
                               category_type TINYINT NOT NULL COMMENT '1-支出 2-收入',
                               description VARCHAR(200) COMMENT '分类描述',
                               create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               source_type_id INT NULL COMMENT '来源类型ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单分类表';

-- 4. 账单总表（核心表）
CREATE TABLE bill (
                      bill_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '账单唯一标识',
                      user_id INT NOT NULL COMMENT '用户ID',
                      source_type_id INT NOT NULL COMMENT '账单来源类型ID',
                      category_id INT NOT NULL COMMENT '账单分类ID',
                      amount DECIMAL(10,2) NOT NULL COMMENT '交易金额（正数）',
                      transaction_time DATETIME NOT NULL COMMENT '交易发生时间',
                      order_amount DECIMAL(10,2) COMMENT '订单金额（商品原价）',
                      voucher_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠券/代金券金额',
                      pay_type TINYINT NOT NULL COMMENT '收支类型：1-支出 2-收入',
                      remark VARCHAR(500) COMMENT '账单备注/商品名称',
                      third_party_order_no VARCHAR(100) COMMENT '第三方订单号',
                      merchant_order_no VARCHAR(100) COMMENT '商户订单号',
                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',

    -- 索引优化
                      INDEX idx_user_id (user_id),
                      INDEX idx_transaction_time (transaction_time),
                      INDEX idx_source_type (source_type_id),
                      INDEX idx_category (category_id),
                      INDEX idx_pay_type (pay_type),
                      UNIQUE INDEX idx_third_party_order (third_party_order_no)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人账单总表';

-- 插入用户数据
INSERT INTO user (username, password, email) VALUES
                                                 ('张三', 'zhangsan123', 'zhangsan@example.com'),
                                                 ('李四', 'lisi456', 'lisi@example.com'),
                                                 ('王五', 'wangwu789', 'wangwu@example.com'),
                                                 ('赵六', 'zhaoliu012', 'zhaoliu@example.com');

-- 插入账单来源类型数据
INSERT INTO bill_source_type (source_name, description) VALUES
                                                            ('支付宝', '支付宝平台账单'),
                                                            ('微信支付', '微信支付平台账单'),
                                                            ('现金', '现金交易'),
                                                            ('银行卡', '银行卡交易'),
                                                            ('信用卡', '信用卡交易');


-- 插入账单分类数据（包含特定来源和通用分类）
INSERT INTO bill_category (category_name, parent_id, category_type, description, source_type_id) VALUES
-- 支出分类 (category_type = 1)
('食品酒水', 0, 1, '餐饮食品相关支出', NULL),
('日常餐饮', 1, 1, '早午晚餐、外卖等', NULL),
('水果零食', 1, 1, '水果、零食、饮料购买', NULL),
('住房物业', 0, 1, '住房相关支出', NULL),
('房租', 4, 1, '房屋租金', NULL),
('水电物业', 4, 1, '水费、电费、物业费', NULL),
('交通出行', 0, 1, '交通相关支出', NULL),
('公共交通', 7, 1, '公交、地铁等', NULL),
('打车租车', 7, 1, '出租车、网约车等', NULL),

-- 支付宝特定分类
('花呗分期', 0, 1, '支付宝花呗分期付款', 1), -- source_type_id=1(支付宝)
('余额宝理财', 0, 2, '支付宝余额宝收益', 1), -- source_type_id=1(支付宝)

-- 微信支付特定分类
('微信红包', 0, 1, '微信发红包、抢红包', 2), -- source_type_id=2(微信支付)
('小程序支付', 0, 1, '微信小程序内消费', 2), -- source_type_id=2(微信支付)

-- 收入分类 (category_type = 2)
('职业收入', 0, 2, '工作职业相关收入', NULL),
('工资薪金', 13, 2, '工资、薪水收入', NULL),
('奖金补贴', 13, 2, '奖金、津贴、补贴等', NULL),
('投资理财', 0, 2, '投资理财收入', NULL),
('利息收入', 16, 2, '存款、理财利息等', NULL);

-- 插入账单总表数据
INSERT INTO bill (user_id, source_type_id, category_id, amount, transaction_time, order_amount, voucher_amount, pay_type, remark, third_party_order_no) VALUES
-- 张三的账单（用户ID=1）
(1, 1, 2, 35.50, '2024-10-10 12:30:00', 35.50, 0.00, 1, '午餐外卖', '20241010123000123456'),
(1, 2, 8, 8.00, '2024-10-10 08:15:00', 8.00, 0.00, 1, '地铁通勤', 'WX20241010081500123456'),
(1, 1, 2, 68.00, '2024-10-09 19:20:00', 75.00, 7.00, 1, '晚餐聚餐', '20241009192000345678'),
(1, 3, 5, 2500.00, '2024-10-05 09:00:00', 2500.00, 0.00, 1, '十月房租', 'CASH202410050900001'),

-- 李四的账单（用户ID=2）
(2, 2, 11, 299.00, '2024-10-08 15:30:00', 299.00, 0.00, 1, '购买衬衫', 'WX20241008153000987654'),
(2, 1, 14, 8500.00, '2024-10-01 10:00:00', 8500.00, 0.00, 2, '九月工资', '20241001100000567890'),
(2, 4, 17, 150.25, '2024-10-07 11:20:00', 150.25, 0.00, 2, '理财利息', 'BANK202410071120001'),

-- 王五的账单（用户ID=3）
(3, 1, 3, 25.80, '2024-10-09 16:45:00', 25.80, 0.00, 1, '超市买水果', '20241009164500765432'),
(3, 5, 9, 45.00, '2024-10-06 20:15:00', 45.00, 0.00, 1, '周末打车', 'CARD202410062015003'),
(3, 2, 15, 2000.00, '2024-10-03 14:00:00', 2000.00, 0.00, 2, '项目奖金', 'WX202410031400008'),

-- 赵六的账单（用户ID=4）
(4, 1, 6, 156.30, '2024-10-04 18:30:00', 156.30, 0.00, 1, '缴纳水电费', '20241004183000432109'),
(4, 2, 12, 88.00, '2024-10-02 13:00:00', 100.00, 12.00, 1, '理发', 'WX202410021300005'),
(4, 1, 14, 12000.00, '2024-09-30 09:00:00', 12000.00, 0.00, 2, '三季度奖金', '202409300900006');





select * from bill;

delete from bill;

select * from bill_source_type;

select * from user;

select * from bill_category;





select u1_0.user_id,u1_0.email,u1_0.password,u1_0.username from user u1_0 where u1_0.username='张三'

