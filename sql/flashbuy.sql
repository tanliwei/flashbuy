/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Database       : flashbuy

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2019-09-07 14:09:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for flashbuy_goods
-- ----------------------------
DROP TABLE IF EXISTS `flashbuy_goods`;
CREATE TABLE `flashbuy_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) DEFAULT NULL,
  `stock_count` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `flashbuy_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flashbuy_goods
-- ----------------------------
INSERT INTO `flashbuy_goods` VALUES ('1', '1', '22', '2019-08-26 20:05:15', '2019-08-28 20:05:18', '0.10');
INSERT INTO `flashbuy_goods` VALUES ('2', '2', '11', '2019-09-05 09:09:55', '2019-09-29 20:05:46', '0.10');

-- ----------------------------
-- Table structure for flashbuy_order
-- ----------------------------
DROP TABLE IF EXISTS `flashbuy_order`;
CREATE TABLE `flashbuy_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_order_goods` (`order_id`,`goods_id`,`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flashbuy_order
-- ----------------------------

-- ----------------------------
-- Table structure for flashbuy_user
-- ----------------------------
DROP TABLE IF EXISTS `flashbuy_user`;
CREATE TABLE `flashbuy_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `head` varchar(255) DEFAULT NULL,
  `register_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `login_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18317055017 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flashbuy_user
-- ----------------------------
INSERT INTO `flashbuy_user` VALUES ('18312341234', 'adniel', '1ce5dd59898565d64eaff30baaaa792a', 'QWERfdsa', '1', '2019-08-31 11:06:44', null, null);
INSERT INTO `flashbuy_user` VALUES ('18317000016', 'daniel', '0e3223d409515063fa61fc94896d75ba', 'fdsfdsffdsf', '1', '2019-08-26 20:31:36', null, null);

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(255) DEFAULT NULL,
  `goods_title` varchar(255) DEFAULT NULL,
  `goods_img` varchar(255) DEFAULT NULL,
  `goods_detail` varchar(255) DEFAULT NULL,
  `goods_price` decimal(10,2) DEFAULT NULL,
  `goods_stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('1', 'iphone7', 'iphonex', '/img/iphonex.png', '拼多多', '1000.00', '20');
INSERT INTO `goods` VALUES ('2', '华为P30', '华为p30', '/img/meta10.png', '淘宝', '2000.00', '20');

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `delivery_addr_id` bigint(20) DEFAULT NULL,
  `goods_name` varchar(255) DEFAULT NULL,
  `goods_count` int(11) DEFAULT NULL,
  `goods_price` decimal(10,2) DEFAULT NULL,
  `order_channel` int(11) DEFAULT NULL,
  `status` int(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('41', '18317055016', '2', null, '华为P30', '1', '0.10', '1', '0', '2019-08-27 08:21:07', null);
INSERT INTO `order_info` VALUES ('42', '18317055016', '2', null, '华为P30', '1', '0.10', '1', '0', '2019-08-27 08:55:43', null);
INSERT INTO `order_info` VALUES ('43', '18312341234', '2', null, '华为P30', '1', '0.10', '1', '0', '2019-09-07 13:41:58', null);
INSERT INTO `order_info` VALUES ('44', '18312341234', '2', null, '华为P30', '1', '0.10', '1', '0', '2019-09-07 13:45:07', null);
INSERT INTO `order_info` VALUES ('45', '18312341234', '2', null, '华为P30', '1', '0.10', '1', '0', '2019-09-07 13:48:40', null);
INSERT INTO `order_info` VALUES ('46', '18312341234', '1', null, 'iphone7', '1', '0.10', '1', '0', '2019-09-07 13:58:43', null);
INSERT INTO `order_info` VALUES ('47', '18312341234', '1', null, 'iphone7', '1', '0.10', '1', '0', '2019-09-07 14:04:31', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
