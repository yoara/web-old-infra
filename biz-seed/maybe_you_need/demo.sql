/*
Navicat MySQL Data Transfer

Source Server         : 测试服务器109
Source Server Version : 50617
Source Host           : 192.168.0.109:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2016-08-25 16:45:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_base_authority_leaf
-- ----------------------------
DROP TABLE IF EXISTS `t_base_authority_leaf`;
CREATE TABLE `t_base_authority_leaf` (
  `FDefinition` varchar(40) NOT NULL COMMENT '定义，枚举',
  `FName` varchar(45) NOT NULL COMMENT '显示名',
  `FKAuthorityNodeId` varchar(40) NOT NULL COMMENT '上级节点id',
  `FOrder` int(11) DEFAULT NULL COMMENT '序号',
  `FIsMenu` tinyint(11) DEFAULT NULL COMMENT '是否显示在菜单页',
  PRIMARY KEY (`FDefinition`),
  KEY `INDEX_BASE_AUTHORITY_FKAUTHORITYTYPEID` (`FKAuthorityNodeId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Table structure for t_base_authority_node
-- ----------------------------
DROP TABLE IF EXISTS `t_base_authority_node`;
CREATE TABLE `t_base_authority_node` (
  `FKey` varchar(45) NOT NULL COMMENT '权限节点标识',
  `FName` varchar(45) NOT NULL COMMENT '显示名',
  `FKParentKey` varchar(45) DEFAULT NULL COMMENT '上级节点菜单',
  `FOrder` int(11) DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`FKey`),
  KEY `INDEX_BASE_AUTHORITY_TYPE_FKPARENTID` (`FKParentKey`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限类别节点表';


-- ----------------------------
-- Table structure for t_base_manager
-- ----------------------------
DROP TABLE IF EXISTS `t_base_manager`;
CREATE TABLE `t_base_manager` (
  `FID` varchar(40) NOT NULL,
  `FPhoneNumber` varchar(45) NOT NULL COMMENT '电话',
  `FName` varchar(45) NOT NULL COMMENT '姓名',
  `FKPositionId` varchar(40) NOT NULL COMMENT '关联岗位id',
  `FPassword` varchar(45) NOT NULL COMMENT '密码',
  `FCreateTime` datetime NOT NULL COMMENT '创建时间',
  `FStatus` varchar(45) NOT NULL COMMENT '状态（ENABLED,DISABLED）',
  PRIMARY KEY (`FID`),
  UNIQUE KEY `INDEX_BASE_MANAGER_PHONENUMBER` (`FPhoneNumber`) USING BTREE,
  KEY `INDEX_BASE_MANAGER_FKPOSTID` (`FKPositionId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营支持平台用户表';

-- ----------------------------
-- Records of t_base_manager
-- ----------------------------
INSERT INTO `t_base_manager` VALUES ('0', 'admin', 'admin', '0', '21232f297a57a5a743894a0e4a801fc3', '2016-05-18 17:37:40', 'ENABLED');
INSERT INTO `t_base_manager` VALUES ('1', 'manager', '子系统用户', '1', 'e10adc3949ba59abbe56e057f20f883e', '2016-04-25 17:10:13', 'ENABLED');

-- ----------------------------
-- Table structure for t_base_manager_authority
-- ----------------------------
CREATE TABLE `t_base_manager_authority` (
  `FID` varchar(40) NOT NULL,
  `FKManagerId` varchar(40) NOT NULL COMMENT 'manager id',
  `FKAuthorityId` varchar(40) NOT NULL COMMENT '权限叶子id',
  `FType` varchar(10) NOT NULL COMMENT '类型，NOT/GET表示不具有或具有某权限，该数据将和关联Position权限数据做并集',
  PRIMARY KEY (`FID`),
  UNIQUE KEY `INDEX_BASE_MANAGER_AUTH_A_PID` (`FKAuthorityId`,`FKManagerId`) USING BTREE,
  KEY `INDEX_BASE_MANAGER_AUTH_MID` (`FKManagerId`) USING BTREE,
  KEY `INDEX_BASE_MANAGER_AUTH_AID` (`FKAuthorityId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='manager与权限关系表';

-- ----------------------------
-- Table structure for t_base_position
-- ----------------------------
DROP TABLE IF EXISTS `t_base_position`;
CREATE TABLE `t_base_position` (
  `FId` varchar(40) NOT NULL,
  `FName` varchar(45) NOT NULL COMMENT '岗位名',
  `FStatus` varchar(45) NOT NULL COMMENT '状态(ENABLED,DISABLED)',
  `FKParentId` varchar(40) DEFAULT NULL COMMENT '自关联父节点ID',
  PRIMARY KEY (`FId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位表';

-- ----------------------------
-- Records of t_base_position
-- ----------------------------
INSERT INTO `t_base_position` VALUES ('0', 'admin', 'ENABLED',null);
INSERT INTO `t_base_position` VALUES ('1', '1号组织', 'ENABLED',null);

-- ----------------------------
-- Table structure for t_base_position_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_base_position_authority`;
CREATE TABLE `t_base_position_authority` (
  `FID` varchar(40) NOT NULL,
  `FKPositionId` varchar(40) NOT NULL COMMENT '岗位id',
  `FKAuthorityId` varchar(40) NOT NULL COMMENT '权限叶子id',
  PRIMARY KEY (`FID`),
  UNIQUE KEY `INDEX_BASE_POSITION_AUTHORITY_POSITIONID` (`FKAuthorityId`,`FKPositionId`) USING BTREE,
  KEY `INDEX_BASE_POSITION_FKPOSITIONID` (`FKPositionId`) USING BTREE,
  KEY `INDEX_BASE_POSITION_FKAUTHORITYID` (`FKAuthorityId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位与权限关系表';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `name` varchar(50) DEFAULT NULL,
  `id` varchar(40) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('user', '1', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `t_user` VALUES ('cici', '2', 'e10adc3949ba59abbe56e057f20f883e');
