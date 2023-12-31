CREATE DATABASE  IF NOT EXISTS `aus-call-center` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `aus-call-center`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: aus-call-center
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `key` varchar(50) DEFAULT NULL COMMENT 'key',
  `value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pid` bigint DEFAULT NULL COMMENT '上级ID',
  `pids` varchar(500) DEFAULT NULL COMMENT '所有上级ID，用逗号分开',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `sort` int unsigned DEFAULT NULL COMMENT '排序',
  `create_user` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`pid`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (1,0,'0','瑞声达集团',0,0,'2023-12-29 09:04:26',NULL,NULL,_binary '\0'),(2,1,'0,1','技术部',0,1,'2023-12-29 15:38:30',NULL,NULL,_binary '\0'),(3,2,'0,1,2','ERP组',0,1,'2023-12-29 15:50:33',NULL,NULL,_binary '\0'),(4,1,'0,1','承运部',0,1,'2023-12-29 16:02:47',NULL,NULL,_binary '\0'),(5,1,'0,1','售后部',0,1,'2023-12-29 16:04:36',NULL,NULL,_binary '\0'),(6,1,'0,1','外呼部',0,1,'2023-12-29 16:16:53',NULL,NULL,_binary '\0'),(7,1,'0,1','400部',0,1,'2023-12-29 16:17:47',NULL,NULL,_binary '\0'),(8,6,'0,1,6','奎文王春霞组',0,1,'2023-12-29 16:19:48',NULL,NULL,_binary '\0'),(9,6,'0,1,6','安丘李霞组',0,1,'2023-12-29 16:21:00',NULL,NULL,_binary '\0'),(11,6,'0,1,6','安丘王蕾组',1,1,'2023-12-29 16:23:28',NULL,NULL,_binary '\0'),(12,6,'0,1,6','青州xx组',0,1,'2023-12-29 16:24:01',NULL,NULL,_binary '\0'),(13,1,'0,1','新媒体',0,1,'2023-12-29 16:49:54',NULL,NULL,_binary '\0'),(14,1,'0,1','节目制作',0,1,'2023-12-29 16:56:52',NULL,NULL,_binary '\0'),(15,7,'0,1,7','安丘王甜甜组',0,1,'2023-12-29 16:58:04',NULL,NULL,_binary '\0'),(16,2,'0,1,2','商城组',0,1,'2023-12-29 16:58:57',NULL,NULL,_binary '\0'),(17,2,'0,1,2','呼叫中心组',0,1,'2023-12-29 16:59:53',NULL,NULL,_binary '\0'),(18,2,'0,1,2','产品组',0,1,'2023-12-29 17:01:40',NULL,NULL,_binary '\0'),(19,2,'0,1,2','测试组',0,1,'2023-12-29 17:03:57',0,'2023-12-30 09:29:39',_binary '\0');
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dict_type_id` bigint unsigned NOT NULL COMMENT '字典类型ID',
  `dict_label` varchar(255) NOT NULL COMMENT '字典标签',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int unsigned DEFAULT NULL COMMENT '排序',
  `create_user` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type_value` (`dict_type_id`,`dict_value`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'女','0','女士',1,1,'2024-01-05 21:27:23',1,'2024-01-05 21:27:49'),(2,1,'男','1','男士',2,1,'2024-01-05 21:27:35',NULL,NULL),(3,1,'保密','2','保密',3,1,'2024-01-05 21:27:45',NULL,NULL),(4,2,'菜单','1','配置路由和菜单权限',1,1,'2024-01-05 21:28:12',1,'2024-01-05 21:28:43'),(5,2,'按钮','2','配置按钮权限',2,1,'2024-01-05 21:28:33',NULL,NULL),(6,3,'停用','0','',1,1,'2024-01-07 15:11:48',1,'2024-01-07 15:12:08'),(7,3,'正常','1','',2,1,'2024-01-07 15:11:54',1,'2024-01-07 15:12:12');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `dict_name` varchar(255) NOT NULL COMMENT '字典名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int unsigned DEFAULT NULL COMMENT '排序',
  `create_user` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dict_type` (`dict_type`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'gender','性别','性别 0女 1男 2保密',1,1,'2024-01-05 21:26:41',NULL,NULL),(2,'menu_type','菜单类型','菜单类型 1菜单 2按钮',2,1,'2024-01-05 21:27:11',NULL,NULL),(3,'user_status','用户状态','用户状态 0停用 1正常',3,1,'2024-01-07 15:11:35',1,'2024-01-07 15:12:02');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log_error`
--

DROP TABLE IF EXISTS `sys_log_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log_error` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `request_uri` varchar(200) DEFAULT NULL COMMENT '请求URI',
  `request_method` varchar(20) DEFAULT NULL COMMENT '请求方式',
  `request_params` text COMMENT '请求参数',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) DEFAULT NULL COMMENT '操作IP',
  `error_info` text COMMENT '异常信息',
  `create_user` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='异常日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log_error`
--

LOCK TABLES `sys_log_error` WRITE;
/*!40000 ALTER TABLE `sys_log_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log_login`
--

DROP TABLE IF EXISTS `sys_log_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log_login` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `operation` tinyint unsigned DEFAULT NULL COMMENT '用户操作   0：用户登录   1：用户退出',
  `status` tinyint unsigned NOT NULL COMMENT '状态  0：失败    1：成功    2：账号已锁定',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) DEFAULT NULL COMMENT '操作IP',
  `create_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `create_user` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='登录日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log_login`
--

LOCK TABLES `sys_log_login` WRITE;
/*!40000 ALTER TABLE `sys_log_login` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log_login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log_operation`
--

DROP TABLE IF EXISTS `sys_log_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log_operation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `request_params` text COMMENT '请求参数',
  `request_time` bigint unsigned NOT NULL COMMENT '请求时长(毫秒)',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) DEFAULT NULL COMMENT '操作IP',
  `status` tinyint unsigned NOT NULL COMMENT '状态  0：失败   1：成功',
  `create_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `create_user` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log_operation`
--

LOCK TABLES `sys_log_operation` WRITE;
/*!40000 ALTER TABLE `sys_log_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int DEFAULT NULL COMMENT '类型   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint NOT NULL,
  `update_user` bigint DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'系统管理','/system','',1,'layui-icon-set',10,'2024-01-06 14:08:45',NULL,1,NULL,0),(2,1,'用户管理','/system/user','',1,'layui-icon-username',1,'2024-01-06 14:24:36','2024-01-06 20:30:17',1,1,0),(3,1,'角色管理','/system/role','',1,'layui-icon-user',2,'2024-01-06 14:34:28','2024-01-06 20:30:25',1,1,0),(4,1,'菜单管理','/system/menu','',1,'layui-icon-spread-left',4,'2024-01-06 14:35:55','2024-01-06 20:30:35',1,1,0),(5,1,'机构管理','/system/organization','',1,'layui-icon-transfer',3,'2024-01-06 14:40:10','2024-01-06 20:30:30',1,1,0),(6,1,'字典管理','/system/dictionary','',1,'layui-icon-read',5,'2024-01-06 14:41:12','2024-01-06 20:30:43',1,1,0),(7,1,'文件管理','/system/file','',1,'layui-icon-file',6,'2024-01-06 14:43:55','2024-01-06 20:30:49',1,1,0),(8,1,'登录日志','/system/login','',1,'layui-icon-date',7,'2024-01-06 14:44:56','2024-01-06 20:30:53',1,1,0),(9,1,'操作日志','/system/option','',1,'layui-icon-survey',8,'2024-01-06 14:45:23','2024-01-06 20:30:56',1,1,0),(10,4,'新建','','sys:menu:save',2,'',0,'2024-01-06 14:58:08',NULL,1,NULL,0),(11,4,'修改','','sys:menu:update',2,'',0,'2024-01-06 14:58:31',NULL,1,NULL,0),(12,4,'删除','','sys:menu:delete',2,'',0,'2024-01-06 14:58:49',NULL,1,NULL,0),(13,4,'查询','','sys:menu:search',2,'',4,'2024-01-06 15:32:48','2024-01-06 15:59:45',1,1,1),(14,3,'新增','','sys:role:save',2,'',1,'2024-01-06 22:36:02',NULL,1,NULL,0),(15,3,'编辑','','sys:role:update',2,'',2,'2024-01-06 22:36:19',NULL,1,NULL,0),(16,3,'删除','','sys:role:delete',2,'',3,'2024-01-06 22:36:34',NULL,1,NULL,0),(17,3,'分配权限','','sys:role:permissions',2,'',4,'2024-01-06 23:04:57','2024-01-07 11:38:27',1,1,1),(18,3,'分配权限','','sys:role:perms',2,'',4,'2024-01-07 11:38:16','2024-01-07 11:38:39',1,1,0),(19,5,'新增','','sys:dept:save',2,'',1,'2024-01-07 13:40:26',NULL,1,NULL,0),(20,5,'编辑','','sys:dept:update',2,'',2,'2024-01-07 13:40:44',NULL,1,NULL,0),(21,5,'删除','','sys:dept:delete',2,'',3,'2024-01-07 13:41:02',NULL,1,NULL,0),(22,6,'新增','','sys:dict:save',2,'',1,'2024-01-07 13:47:42',NULL,1,NULL,0),(23,6,'编辑','','sys:dict:update',2,'',2,'2024-01-07 13:49:02',NULL,1,NULL,0),(24,6,'删除','','sys:dict:delete',2,'',3,'2024-01-07 13:49:20',NULL,1,NULL,0),(25,6,'新增字典值','','sys:dictdata:save',2,'',4,'2024-01-07 13:49:57','2024-01-07 13:51:44',1,1,0),(26,6,'编辑字典值','','sys:dictdata:update',2,'',5,'2024-01-07 13:52:18',NULL,1,NULL,0),(27,6,'删除字典值','','sys:dictdata:delete',2,'',6,'2024-01-07 13:52:45',NULL,1,NULL,0),(28,2,'重置密码','','sys:user:resetpassword',2,'',0,'2024-01-07 14:21:08',NULL,1,NULL,0),(29,2,'新增','','sys:user:save',2,'',1,'2024-01-07 14:21:28',NULL,1,NULL,0),(30,2,'编辑','','sys:user:update',2,'',2,'2024-01-07 14:54:00',NULL,1,NULL,0),(31,2,'锁定/解锁','','sys:user:lock',2,'',3,'2024-01-07 14:54:36',NULL,1,NULL,0),(32,2,'重置密码','','sys:user:resetpassword',2,'',4,'2024-01-07 14:55:09','2024-01-07 15:57:13',1,1,1),(33,2,'删除','','sys:user:delete',2,'',5,'2024-01-07 14:56:01',NULL,1,NULL,0),(34,8,'查看','','',2,'',0,'2024-01-07 15:58:54','2024-01-07 15:59:34',1,1,1),(35,8,'查询','','',2,'',0,'2024-01-07 16:40:46','2024-01-07 16:47:19',1,1,1);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint NOT NULL,
  `update_user` bigint DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'管理员','管理员角色','2024-01-06 22:37:04','2024-01-06 22:41:02',1,1,0),(2,'话务员','话务员角色','2024-01-06 22:41:14','2024-01-06 23:09:00',1,1,0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint NOT NULL,
  `update_user` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色与菜单对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (64,2,10,'2024-01-07 18:22:06',NULL,1,NULL),(65,1,1,'2024-01-07 18:22:11',NULL,1,NULL),(66,1,2,'2024-01-07 18:22:11',NULL,1,NULL),(67,1,3,'2024-01-07 18:22:11',NULL,1,NULL),(68,1,14,'2024-01-07 18:22:11',NULL,1,NULL),(69,1,15,'2024-01-07 18:22:11',NULL,1,NULL),(70,1,16,'2024-01-07 18:22:11',NULL,1,NULL),(71,1,18,'2024-01-07 18:22:11',NULL,1,NULL),(72,1,5,'2024-01-07 18:22:11',NULL,1,NULL),(73,1,6,'2024-01-07 18:22:11',NULL,1,NULL),(74,1,7,'2024-01-07 18:22:11',NULL,1,NULL),(75,1,8,'2024-01-07 18:22:11',NULL,1,NULL),(76,1,9,'2024-01-07 18:22:11',NULL,1,NULL);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `real_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `gender` tinyint unsigned DEFAULT '1' COMMENT '性别 0女 1男 2保密',
  `super_admin` tinyint unsigned DEFAULT NULL COMMENT '超级管理员   0：否   1：是',
  `status` tinyint DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint NOT NULL,
  `update_user` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_dept` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','管理员','','{bcrypt}$2a$10$umbdzri52hrdtA6KMcmakOOCFbGku9LQHdZlWAUwUMEDYYNPKayta','4b083d8bff412020','test@xxx.com','13612345678',1,1,1,1,'2023-11-11 11:11:11',NULL,0,NULL,_binary '\0');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint NOT NULL,
  `update_user` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与角色对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,2,1,'2024-01-07 17:58:59',NULL,1,NULL),(2,3,1,'2024-01-07 18:32:25',NULL,1,NULL);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-07 18:34:46
