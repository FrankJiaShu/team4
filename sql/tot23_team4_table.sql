DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(20) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `permissions` int DEFAULT '0' COMMENT '权限控制 0-无权限 1-有权限',
  `capicity` int DEFAULT NULL COMMENT '用户容量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';


DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `file_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `file_path` varchar(100) DEFAULT NULL COMMENT '真实路径',
  `file_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  `file_name` varchar(100) DEFAULT NULL COMMENT '上传文件名',
  `file_size` varchar(100) DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件表';
