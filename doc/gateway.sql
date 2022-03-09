/*
 Navicat Premium Data Transfer

 Source Server         : 111.229.170.57
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : 111.229.170.57:3306
 Source Schema         : gateway

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 08/03/2022 09:55:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gateway
-- ----------------------------
DROP TABLE IF EXISTS `gateway`;
CREATE TABLE `gateway`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `uri` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目标服务',
  `predicates` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '断言-匹配规则',
  `filters` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过滤-替换目标访问地址',
  `text` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `reg_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务id',
  `enabled` int(255) NULL DEFAULT NULL COMMENT '启用',
  `oauth2` int(255) NULL DEFAULT NULL COMMENT '授权',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gateway
-- ----------------------------
INSERT INTO `gateway` VALUES (123, 'lb://coupon-platform', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/platform/**\"}}]', '[{\"name\":\"StripPrefix\",\"args\":{\"_genkey_0\":\"1\"}}]', 'coupon-platform', '淘券-平台对接', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
