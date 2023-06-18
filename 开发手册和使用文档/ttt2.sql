/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : localhost:3306
 Source Schema         : ttt2

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 15/09/2022 10:06:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data
-- ----------------------------
DROP TABLE IF EXISTS `data`;
CREATE TABLE `data`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小组id',
  `line_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导线id',
  `sj` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传数据',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `data_ibfk_1`(`group_id`) USING BTREE,
  INDEX `data_ibfk_2`(`line_id`) USING BTREE,
  CONSTRAINT `data_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `group_management` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `data_ibfk_2` FOREIGN KEY (`line_id`) REFERENCES `el_line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_2
-- ----------------------------
DROP TABLE IF EXISTS `data_2`;
CREATE TABLE `data_2`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小组id',
  `line_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导线id',
  `sj` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传数据',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `data_2_ibfk_1`(`group_id`) USING BTREE,
  INDEX `data_2_ibfk_2`(`line_id`) USING BTREE,
  CONSTRAINT `data_2_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `group_management` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `data_2_ibfk_2` FOREIGN KEY (`line_id`) REFERENCES `line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_solution
-- ----------------------------
DROP TABLE IF EXISTS `data_solution`;
CREATE TABLE `data_solution`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '测站id',
  `fore_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前视站点id',
  `back_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后视站点id',
  `avg_distance` double(8, 4) NULL DEFAULT NULL COMMENT '平均平距（前视）（米）',
  `avg_value_deg` int(11) NULL DEFAULT NULL COMMENT '各测回平均角值度',
  `avg_value_min` int(11) NULL DEFAULT NULL COMMENT '各测回平均角值分',
  `avg_value_sec` double(3, 1) NULL DEFAULT NULL COMMENT '各测回平均角值秒',
  `turning_ang_radian` double(10, 9) NULL DEFAULT NULL COMMENT '转折角弧度',
  `cor_ang_radian` double(10, 9) NULL DEFAULT NULL COMMENT '改正后的转折角弧度',
  `azimuth_ang_deg` int(11) NULL DEFAULT NULL COMMENT '方位角度（前视）',
  `azimuth_ang_min` int(11) NULL DEFAULT NULL COMMENT '方位角分（前视）',
  `azimuth_ang_sec` double(3, 1) NULL DEFAULT NULL COMMENT '方位角秒（前视）',
  `azimuth_ang_radian` double(10, 9) NULL DEFAULT NULL COMMENT '方位角弧度（前视）',
  `incre_coord_X` double(20, 4) NULL DEFAULT 0.0000 COMMENT '坐标增量X（米）',
  `incre_coord_Y` double(20, 4) NULL DEFAULT 0.0000 COMMENT '坐标增量Y（米）',
  `cor_inc_coord_X` double(20, 4) NULL DEFAULT 0.0000 COMMENT '改正后的坐标增量X（米）',
  `cor_inc_coord_Y` double(20, 4) NULL DEFAULT 0.0000 COMMENT '改正后的坐标增量Y（米）',
  `final_coord_x` double(20, 4) NULL DEFAULT NULL COMMENT '坐标结果x（米）',
  `final_coord_y` double(20, 4) NULL DEFAULT NULL COMMENT '坐标结果y（米）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `数据解算的测站ID`(`station_id`) USING BTREE,
  INDEX `data_solution_ibfk_2`(`fore_stn_id`) USING BTREE,
  INDEX `data_solution_ibfk_3`(`back_stn_id`) USING BTREE,
  CONSTRAINT `data_solution_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据解算' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for el_data_solution
-- ----------------------------
DROP TABLE IF EXISTS `el_data_solution`;
CREATE TABLE `el_data_solution`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '测站id',
  `fore_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前视站点id',
  `back_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后视站点id',
  `height_instrument_fore_avg` double(20, 4) NULL DEFAULT NULL COMMENT '各测回平均仪器高(m)(测前站时)',
  `height_lens_fore_avg` double(20, 4) NULL DEFAULT NULL COMMENT '各测回平均镜高(m)(测前站时)',
  `vertical_ang_deg_fore_avg` int(11) NULL DEFAULT NULL COMMENT '各测回平均垂直角度(与前站)',
  `vertical_ang_min_fore_avg` int(11) NULL DEFAULT NULL COMMENT '各测回平均垂直角分(与前站)',
  `vertical_ang_sec_fore_avg` double(3, 1) NULL DEFAULT NULL COMMENT '各测回平均垂直角秒(与前站)',
  `vertical_ang_sign_fore` int(11) NULL DEFAULT NULL COMMENT '垂直角的符号(与前站)(1正 -1负)',
  `vertical_ang_radian_fore` double(10, 9) NULL DEFAULT NULL COMMENT '垂直角弧度(与前站)',
  `vertical_ang_radian_fore_cor` double(10, 9) NULL DEFAULT NULL COMMENT '改正后的垂直角弧度(与前站)',
  `elevation_diff_fore` double(20, 4) NULL DEFAULT 0.0000 COMMENT '高差(m)(与前站)',
  `elevation_diff_fore_cor` double(20, 4) NULL DEFAULT 0.0000 COMMENT '改正后的高差(m)(与前站)',
  `elevation_fore` double(20, 4) NULL DEFAULT NULL COMMENT '高程(m)(前视)',
  `height_instrument_back_avg` double(20, 4) NULL DEFAULT NULL COMMENT '各测回平均仪器高(m)(测后站时)',
  `height_lens_back_avg` double(20, 4) NULL DEFAULT NULL COMMENT '各测回平均镜高(m)(测后站时)',
  `vertical_ang_deg_back_avg` int(11) NULL DEFAULT NULL COMMENT '各测回平均垂直角度(与后站)',
  `vertical_ang_min_back_avg` int(11) NULL DEFAULT NULL COMMENT '各测回平均垂直角分(与后站)',
  `vertical_ang_sec_back_avg` double(3, 1) NULL DEFAULT NULL COMMENT '各测回平均垂直角秒(与后站)',
  `vertical_ang_sign_back` int(11) NULL DEFAULT NULL COMMENT '垂直角的符号(与后站)(1正 -1负)',
  `vertical_ang_radian_back` double(10, 9) NULL DEFAULT NULL COMMENT '垂直角弧度(与后站)',
  `vertical_ang_radian_back_cor` double(10, 9) NULL DEFAULT NULL COMMENT '改正后的垂直角弧度(与后站)',
  `elevation_diff_back` double(20, 4) NULL DEFAULT 0.0000 COMMENT '高差(m)(与后站)',
  `elevation_diff_back_cor` double(20, 4) NULL DEFAULT 0.0000 COMMENT '改正后的高差(m)(与后站)',
  `distance_fore_avg` double(20, 4) NULL DEFAULT NULL COMMENT '平均平距(m)(与前站)',
  `elevation_back` double(20, 4) NULL DEFAULT NULL COMMENT '高程(m)(后视)',
  `elevation` double(20, 4) NULL DEFAULT NULL COMMENT '高程(m)',
  `err_eleva_diff_round_trip_fore` double(20, 4) NULL DEFAULT NULL COMMENT '往返测高差较差(mm)(与前站)',
  `err_eleva_diff_round_trip_fore_norm` double(20, 4) NULL DEFAULT NULL COMMENT '往返测高差较差标准(mm)(与前站)',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `数据解算的测站ID`(`station_id`) USING BTREE,
  INDEX `data_solution_ibfk_2`(`fore_stn_id`) USING BTREE,
  INDEX `data_solution_ibfk_3`(`back_stn_id`) USING BTREE,
  CONSTRAINT `el_data_solution_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `el_station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高程——数据解算' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for el_indirect_adjustment
-- ----------------------------
DROP TABLE IF EXISTS `el_indirect_adjustment`;
CREATE TABLE `el_indirect_adjustment`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '测站id',
  `fore_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前视站点id',
  `back_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后视站点id',
  `elevation_diff_fore_cor` double(20, 4) NULL DEFAULT 0.0000 COMMENT '改正后的高差(m)(前视)',
  `elevation_diff_back_cor` double(20, 4) NULL DEFAULT 0.0000 COMMENT '改正后的高差(m)(后视)',
  `elevation_fore` double(20, 4) NULL DEFAULT NULL COMMENT '高程(m)(前视)',
  `elevation_back` double(20, 4) NULL DEFAULT NULL COMMENT '高程(m)(后视)',
  `elevation` double(20, 4) NULL DEFAULT NULL COMMENT '高程(m)',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `数据解算的测站ID`(`station_id`) USING BTREE,
  INDEX `data_solution_ibfk_2`(`fore_stn_id`) USING BTREE,
  INDEX `data_solution_ibfk_3`(`back_stn_id`) USING BTREE,
  CONSTRAINT `el_indirect_adjustment_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `el_station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高程——间接平差' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for el_line
-- ----------------------------
DROP TABLE IF EXISTS `el_line`;
CREATE TABLE `el_line`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小组id',
  `line_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线名称',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `小组ID`(`group_id`) USING BTREE,
  CONSTRAINT `el_line_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `group_management` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高程——导线' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for el_observe_data
-- ----------------------------
DROP TABLE IF EXISTS `el_observe_data`;
CREATE TABLE `el_observe_data`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '测站id',
  `fore_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '前视站点id',
  `back_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '后视站点id',
  `observe_round` int(11) NOT NULL COMMENT '测回',
  `height_instrument_fore` double(20, 4) NOT NULL COMMENT '仪器高(m)(测前站时)',
  `height_lens_fore` double(20, 4) NOT NULL COMMENT '镜高(m)(测前站时)',
  `vertical_ang_sign_fore` int(11) NOT NULL COMMENT '垂直角的符号(与前站)(1正 -1负)',
  `vertical_ang_deg_fore_str` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '带符号的垂直角度(与前站)',
  `vertical_ang_deg_fore` int(11) NOT NULL COMMENT '垂直角度(与前站)',
  `vertical_ang_min_fore` int(11) NOT NULL COMMENT '垂直角分(与前站)',
  `vertical_ang_sec_fore` double(3, 1) NOT NULL COMMENT '垂直角秒(与前站)',
  `distance_fore` double(20, 4) NOT NULL COMMENT '平距(m)(与前站)',
  `height_instrument_back` double(20, 4) NOT NULL COMMENT '仪器高(m)(测后站时)',
  `height_lens_back` double(20, 4) NOT NULL COMMENT '镜高(m)(测后站时)',
  `vertical_ang_sign_back` int(11) NOT NULL COMMENT '垂直角的符号(与后站)(1正 -1负)',
  `vertical_ang_deg_back_str` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '带符号的垂直角度(与后站)',
  `vertical_ang_deg_back` int(11) NOT NULL COMMENT '垂直角度(与后站)',
  `vertical_ang_min_back` int(11) NOT NULL COMMENT '垂直角分(与后站)',
  `vertical_ang_sec_back` double(3, 1) NOT NULL COMMENT '垂直角秒(与后站)',
  `distance_back` double(20, 4) NOT NULL COMMENT '平距(m)(与后站)',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `观测数据的测站ID`(`station_id`) USING BTREE,
  INDEX `fore_stn_id`(`fore_stn_id`) USING BTREE,
  INDEX `back_stn_id`(`back_stn_id`) USING BTREE,
  CONSTRAINT `el_observe_data_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `el_station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高程——观测数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for el_origin_data
-- ----------------------------
DROP TABLE IF EXISTS `el_origin_data`;
CREATE TABLE `el_origin_data`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `origin_station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '起始测站id',
  `origin_elevation` double(20, 4) NOT NULL COMMENT '起始高程(m)',
  `end_station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '终点测站id',
  `end_elevation` double(20, 4) NOT NULL COMMENT '终点高程(m)',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_module_status`(`status`) USING BTREE,
  INDEX `测站ID`(`origin_station_id`) USING BTREE,
  INDEX `end_station_id`(`end_station_id`) USING BTREE,
  CONSTRAINT `el_origin_data_ibfk_1` FOREIGN KEY (`origin_station_id`) REFERENCES `el_station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `el_origin_data_ibfk_2` FOREIGN KEY (`end_station_id`) REFERENCES `el_station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高程——起始数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for el_result_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `el_result_evaluation`;
CREATE TABLE `el_result_evaluation`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `line_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线id',
  `traverse_class` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导线等级(二等、三等、四等、五等)',
  `is_qqc` int(11) NULL DEFAULT NULL COMMENT '是否考虑球气差(1是 0否)',
  `coefficient_k` double(20, 4) NULL DEFAULT NULL COMMENT '大气折光系数',
  `coefficient_r` double(20, 4) NULL DEFAULT NULL COMMENT '地球半径(km)',
  `full_mid_err_eleva_diff_per_km` double(20, 4) NULL DEFAULT NULL COMMENT '每千米高差全中误差(mm)',
  `full_mid_err_eleva_diff_per_km_norm` double(20, 4) NULL DEFAULT NULL COMMENT '每千米高差全中误差标准(mm)',
  `clos_err_line` double(20, 4) NULL DEFAULT NULL COMMENT '导线闭合差(mm)',
  `clos_err_line_norm` double(20, 4) NULL DEFAULT NULL COMMENT '导线闭合差标准(mm)',
  `result_evaluation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果评价(√ or 不通过原因)',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态(0正常 1删除 2停用)',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `结算结果的导线ID`(`line_id`) USING BTREE,
  CONSTRAINT `el_result_evaluation_ibfk_1` FOREIGN KEY (`line_id`) REFERENCES `el_line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高程——结果评定' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for el_result_evaluation_strict
-- ----------------------------
DROP TABLE IF EXISTS `el_result_evaluation_strict`;
CREATE TABLE `el_result_evaluation_strict`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `line_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线id',
  `traverse_class` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导线等级(二等、三等、四等、五等)',
  `mid_err_unit_weight` double(30, 4) NULL DEFAULT NULL COMMENT '单位权中误差（毫米）',
  `result_evaluation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果评价（√ or 不通过原因）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `结算结果的导线ID`(`line_id`) USING BTREE,
  CONSTRAINT `el_result_evaluation_strict_ibfk_1` FOREIGN KEY (`line_id`) REFERENCES `el_line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高程——结果评定（严密）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for el_station
-- ----------------------------
DROP TABLE IF EXISTS `el_station`;
CREATE TABLE `el_station`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `line_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线id',
  `station_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '测站名称',
  `fore_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前视站点id',
  `back_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后视站点id',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `导线圈ID`(`line_id`) USING BTREE,
  CONSTRAINT `el_station_ibfk_1` FOREIGN KEY (`line_id`) REFERENCES `el_line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高程——测站' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for group_management
-- ----------------------------
DROP TABLE IF EXISTS `group_management`;
CREATE TABLE `group_management`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `group_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小组名称',
  `group_leader` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '负责人',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for indirect_adjustment
-- ----------------------------
DROP TABLE IF EXISTS `indirect_adjustment`;
CREATE TABLE `indirect_adjustment`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '测站id',
  `fore_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前视站点id',
  `back_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后视站点id',
  `distance` double(8, 4) NULL DEFAULT NULL COMMENT '平距（前视）（米）',
  `strict_distance` double(8, 4) NULL DEFAULT NULL COMMENT '严密平距（前视）（米）',
  `ang_deg` int(11) NULL DEFAULT NULL COMMENT '转折角度',
  `ang_min` int(11) NULL DEFAULT NULL COMMENT '转折角分',
  `ang_sec` double(3, 1) NULL DEFAULT NULL COMMENT '转折角秒',
  `strict_ang_deg` int(11) NULL DEFAULT NULL COMMENT '严密转折角度',
  `strict_ang_min` int(11) NULL DEFAULT NULL COMMENT '严密转折角分',
  `strict_ang_sec` double(3, 1) NULL DEFAULT NULL COMMENT '严密转折角秒',
  `final_coord_x` double(20, 4) NULL DEFAULT NULL COMMENT '坐标结果x（米）',
  `final_coord_y` double(20, 4) NULL DEFAULT NULL COMMENT '坐标结果y（米）',
  `strict_coord_x` double(20, 4) NULL DEFAULT NULL COMMENT '严密坐标x（米）',
  `strict_coord_y` double(20, 4) NULL DEFAULT NULL COMMENT '严密坐标y（米）',
  `medium_error_x` double(30, 20) NULL DEFAULT NULL COMMENT '待定点坐标平差值的中误差x（毫米）',
  `medium_error_y` double(30, 20) NULL DEFAULT NULL COMMENT '待定点坐标平差值的中误差y（毫米）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `station_id`(`station_id`) USING BTREE,
  CONSTRAINT `indirect_adjustment_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '间接平差' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_gen_table
-- ----------------------------
DROP TABLE IF EXISTS `js_gen_table`;
CREATE TABLE `js_gen_table`  (
  `table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '实体类名称',
  `comments` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表说明',
  `parent_table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联父表的表名',
  `parent_table_fk_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本表关联父表的外键名',
  `data_source_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源名称',
  `tpl_category` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '使用的模板',
  `package_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `sub_module_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成子模块名',
  `function_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_name_simple` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成功能名（简写）',
  `function_author` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_base_dir` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成基础路径',
  `options` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`table_name`) USING BTREE,
  INDEX `idx_gen_table_ptn`(`parent_table_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `js_gen_table_column`;
CREATE TABLE `js_gen_table_column`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `column_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '列名',
  `column_sort` decimal(10, 0) NULL DEFAULT NULL COMMENT '列排序（升序）',
  `column_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型',
  `column_label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列标签名',
  `comments` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '列备注说明',
  `attr_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类的属性名',
  `attr_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类的属性类型',
  `is_pk` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否主键',
  `is_null` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否可为空',
  `is_insert` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否插入字段',
  `is_update` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否更新字段',
  `is_list` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否列表字段',
  `is_query` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否查询字段',
  `query_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询方式',
  `is_edit` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否编辑字段',
  `show_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单类型',
  `options` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_gen_table_column_tn`(`table_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成表列' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `js_job_blob_triggers`;
CREATE TABLE `js_job_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `js_job_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `js_job_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_calendars
-- ----------------------------
DROP TABLE IF EXISTS `js_job_calendars`;
CREATE TABLE `js_job_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `js_job_cron_triggers`;
CREATE TABLE `js_job_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `js_job_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `js_job_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `js_job_fired_triggers`;
CREATE TABLE `js_job_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `FIRED_TIME` bigint(20) NOT NULL,
  `SCHED_TIME` bigint(20) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_job_details
-- ----------------------------
DROP TABLE IF EXISTS `js_job_job_details`;
CREATE TABLE `js_job_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_locks
-- ----------------------------
DROP TABLE IF EXISTS `js_job_locks`;
CREATE TABLE `js_job_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `js_job_paused_trigger_grps`;
CREATE TABLE `js_job_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `js_job_scheduler_state`;
CREATE TABLE `js_job_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint(20) NOT NULL,
  `CHECKIN_INTERVAL` bigint(20) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `js_job_simple_triggers`;
CREATE TABLE `js_job_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REPEAT_COUNT` bigint(20) NOT NULL,
  `REPEAT_INTERVAL` bigint(20) NOT NULL,
  `TIMES_TRIGGERED` bigint(20) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `js_job_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `js_job_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `js_job_simprop_triggers`;
CREATE TABLE `js_job_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `js_job_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `js_job_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_job_triggers
-- ----------------------------
DROP TABLE IF EXISTS `js_job_triggers`;
CREATE TABLE `js_job_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(20) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(20) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `START_TIME` bigint(20) NOT NULL,
  `END_TIME` bigint(20) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(6) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `js_job_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `js_job_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_area
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_area`;
CREATE TABLE `js_sys_area`  (
  `area_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区域编码',
  `parent_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10, 0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_sorts` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有级别排序号',
  `tree_leaf` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否最末级',
  `tree_level` decimal(4, 0) NOT NULL COMMENT '层次级别',
  `tree_names` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全节点名',
  `area_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区域名称',
  `area_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域类型',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`area_code`) USING BTREE,
  INDEX `idx_sys_area_pc`(`parent_code`) USING BTREE,
  INDEX `idx_sys_area_ts`(`tree_sort`) USING BTREE,
  INDEX `idx_sys_area_status`(`status`) USING BTREE,
  INDEX `idx_sys_area_pcs`(`parent_codes`) USING BTREE,
  INDEX `idx_sys_area_tss`(`tree_sorts`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行政区划' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_company
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_company`;
CREATE TABLE `js_sys_company`  (
  `company_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司编码',
  `parent_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10, 0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_sorts` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有级别排序号',
  `tree_leaf` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否最末级',
  `tree_level` decimal(4, 0) NOT NULL COMMENT '层次级别',
  `tree_names` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全节点名',
  `view_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司代码',
  `company_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司名称',
  `full_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司全称',
  `area_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域编码',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `corp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '租户代码',
  `corp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSite' COMMENT '租户名称',
  `extend_s1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 1',
  `extend_s2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 2',
  `extend_s3` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 3',
  `extend_s4` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 4',
  `extend_s5` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 5',
  `extend_s6` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 6',
  `extend_s7` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 7',
  `extend_s8` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 8',
  `extend_i1` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 1',
  `extend_i2` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 2',
  `extend_i3` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 3',
  `extend_i4` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 4',
  `extend_f1` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 1',
  `extend_f2` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 2',
  `extend_f3` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 3',
  `extend_f4` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 4',
  `extend_d1` datetime NULL DEFAULT NULL COMMENT '扩展 Date 1',
  `extend_d2` datetime NULL DEFAULT NULL COMMENT '扩展 Date 2',
  `extend_d3` datetime NULL DEFAULT NULL COMMENT '扩展 Date 3',
  `extend_d4` datetime NULL DEFAULT NULL COMMENT '扩展 Date 4',
  PRIMARY KEY (`company_code`) USING BTREE,
  INDEX `idx_sys_company_cc`(`corp_code`) USING BTREE,
  INDEX `idx_sys_company_pc`(`parent_code`) USING BTREE,
  INDEX `idx_sys_company_ts`(`tree_sort`) USING BTREE,
  INDEX `idx_sys_company_status`(`status`) USING BTREE,
  INDEX `idx_sys_company_vc`(`view_code`) USING BTREE,
  INDEX `idx_sys_company_pcs`(`parent_codes`) USING BTREE,
  INDEX `idx_sys_company_tss`(`tree_sorts`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公司表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_company_office
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_company_office`;
CREATE TABLE `js_sys_company_office`  (
  `company_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司编码',
  `office_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构编码',
  PRIMARY KEY (`company_code`, `office_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公司部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_config`;
CREATE TABLE `js_sys_config`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `config_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `config_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数键',
  `config_value` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数值',
  `is_sys` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统内置（1是 0否）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_sys_config_key`(`config_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_dict_data`;
CREATE TABLE `js_sys_dict_data`  (
  `dict_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编码',
  `parent_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10, 0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_sorts` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有级别排序号',
  `tree_leaf` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否最末级',
  `tree_level` decimal(4, 0) NOT NULL COMMENT '层次级别',
  `tree_names` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全节点名',
  `dict_label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类型',
  `is_sys` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统内置（1是 0否）',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典描述',
  `css_style` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'css样式（如：color:red)',
  `css_class` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'css类名（如：red）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `corp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '租户代码',
  `corp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSite' COMMENT '租户名称',
  `extend_s1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 1',
  `extend_s2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 2',
  `extend_s3` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 3',
  `extend_s4` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 4',
  `extend_s5` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 5',
  `extend_s6` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 6',
  `extend_s7` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 7',
  `extend_s8` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 8',
  `extend_i1` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 1',
  `extend_i2` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 2',
  `extend_i3` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 3',
  `extend_i4` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 4',
  `extend_f1` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 1',
  `extend_f2` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 2',
  `extend_f3` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 3',
  `extend_f4` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 4',
  `extend_d1` datetime NULL DEFAULT NULL COMMENT '扩展 Date 1',
  `extend_d2` datetime NULL DEFAULT NULL COMMENT '扩展 Date 2',
  `extend_d3` datetime NULL DEFAULT NULL COMMENT '扩展 Date 3',
  `extend_d4` datetime NULL DEFAULT NULL COMMENT '扩展 Date 4',
  PRIMARY KEY (`dict_code`) USING BTREE,
  INDEX `idx_sys_dict_data_cc`(`corp_code`) USING BTREE,
  INDEX `idx_sys_dict_data_dt`(`dict_type`) USING BTREE,
  INDEX `idx_sys_dict_data_pc`(`parent_code`) USING BTREE,
  INDEX `idx_sys_dict_data_status`(`status`) USING BTREE,
  INDEX `idx_sys_dict_data_pcs`(`parent_codes`) USING BTREE,
  INDEX `idx_sys_dict_data_ts`(`tree_sort`) USING BTREE,
  INDEX `idx_sys_dict_data_tss`(`tree_sorts`) USING BTREE,
  INDEX `idx_sys_dict_data_dv`(`dict_value`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_dict_type`;
CREATE TABLE `js_sys_dict_type`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类型',
  `is_sys` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否系统字典',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_dict_type_is`(`is_sys`) USING BTREE,
  INDEX `idx_sys_dict_type_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_employee
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_employee`;
CREATE TABLE `js_sys_employee`  (
  `emp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工编码',
  `emp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工姓名',
  `emp_name_en` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工英文名',
  `emp_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工工号',
  `office_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构编码',
  `office_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构名称',
  `company_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0在职 1删除 2离职）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `corp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '租户代码',
  `corp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSite' COMMENT '租户名称',
  PRIMARY KEY (`emp_code`) USING BTREE,
  INDEX `idx_sys_employee_cco`(`company_code`) USING BTREE,
  INDEX `idx_sys_employee_cc`(`corp_code`) USING BTREE,
  INDEX `idx_sys_employee_ud`(`update_date`) USING BTREE,
  INDEX `idx_sys_employee_oc`(`office_code`) USING BTREE,
  INDEX `idx_sys_employee_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '员工表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_employee_office
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_employee_office`;
CREATE TABLE `js_sys_employee_office`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `emp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工编码',
  `office_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构编码',
  `post_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '员工附属机构关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_employee_post
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_employee_post`;
CREATE TABLE `js_sys_employee_post`  (
  `emp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工编码',
  `post_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
  PRIMARY KEY (`emp_code`, `post_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '员工与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_file_entity
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_file_entity`;
CREATE TABLE `js_sys_file_entity`  (
  `file_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件编号',
  `file_md5` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件MD5',
  `file_path` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件相对路径',
  `file_content_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件内容类型',
  `file_extension` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件后缀扩展名',
  `file_size` decimal(31, 0) NOT NULL COMMENT '文件大小(单位B)',
  `file_meta` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件信息(JSON格式)',
  PRIMARY KEY (`file_id`) USING BTREE,
  INDEX `idx_sys_file_entity_md5`(`file_md5`) USING BTREE,
  INDEX `idx_sys_file_entity_size`(`file_size`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件实体表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_file_upload
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_file_upload`;
CREATE TABLE `js_sys_file_upload`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `file_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件编号',
  `file_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `file_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件分类（image、media、file）',
  `file_sort` decimal(10, 0) NULL DEFAULT NULL COMMENT '文件排序（升序）',
  `biz_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `biz_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_file_biz_ft`(`file_type`) USING BTREE,
  INDEX `idx_sys_file_biz_fi`(`file_id`) USING BTREE,
  INDEX `idx_sys_file_biz_status`(`status`) USING BTREE,
  INDEX `idx_sys_file_biz_cb`(`create_by`) USING BTREE,
  INDEX `idx_sys_file_biz_ud`(`update_date`) USING BTREE,
  INDEX `idx_sys_file_biz_bt`(`biz_type`) USING BTREE,
  INDEX `idx_sys_file_biz_bk`(`biz_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件上传表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_job
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_job`;
CREATE TABLE `js_sys_job`  (
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务组名',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务描述',
  `invoke_target` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Cron执行表达式',
  `misfire_instruction` decimal(1, 0) NOT NULL COMMENT '计划执行错误策略',
  `concurrent` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否并发执行',
  `instance_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSiteScheduler' COMMENT '集群的实例名字',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2暂停）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`job_name`, `job_group`) USING BTREE,
  INDEX `idx_sys_job_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_job_log`;
CREATE TABLE `js_sys_job_log`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务组名',
  `job_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型',
  `job_event` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志事件',
  `job_message` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `is_exception` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否异常',
  `exception_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常信息',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_job_log_jn`(`job_name`) USING BTREE,
  INDEX `idx_sys_job_log_jg`(`job_group`) USING BTREE,
  INDEX `idx_sys_job_log_t`(`job_type`) USING BTREE,
  INDEX `idx_sys_job_log_e`(`job_event`) USING BTREE,
  INDEX `idx_sys_job_log_ie`(`is_exception`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_lang
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_lang`;
CREATE TABLE `js_sys_lang`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `module_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '归属模块',
  `lang_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '语言编码',
  `lang_text` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '语言译文',
  `lang_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '语言类型',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_lang_code`(`lang_code`) USING BTREE,
  INDEX `idx_sys_lang_type`(`lang_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '国际化语言' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_log
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_log`;
CREATE TABLE `js_sys_log`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `log_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志类型',
  `log_title` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志标题',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_by_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `request_uri` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求URI',
  `request_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作方式',
  `request_params` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作提交的数据',
  `diff_modify_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '新旧数据比较结果',
  `biz_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `biz_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `remote_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作IP地址',
  `server_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求服务器地址',
  `is_exception` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否异常',
  `exception_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常信息',
  `user_agent` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  `device_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称/操作系统',
  `browser_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '浏览器名称',
  `execute_time` decimal(19, 0) NULL DEFAULT NULL COMMENT '执行时间',
  `corp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '租户代码',
  `corp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSite' COMMENT '租户名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_log_cb`(`create_by`) USING BTREE,
  INDEX `idx_sys_log_cc`(`corp_code`) USING BTREE,
  INDEX `idx_sys_log_lt`(`log_type`) USING BTREE,
  INDEX `idx_sys_log_bk`(`biz_key`) USING BTREE,
  INDEX `idx_sys_log_bt`(`biz_type`) USING BTREE,
  INDEX `idx_sys_log_ie`(`is_exception`) USING BTREE,
  INDEX `idx_sys_log_cd`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_menu`;
CREATE TABLE `js_sys_menu`  (
  `menu_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单编码',
  `parent_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10, 0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_sorts` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有级别排序号',
  `tree_leaf` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否最末级',
  `tree_level` decimal(4, 0) NOT NULL COMMENT '层次级别',
  `tree_names` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全节点名',
  `menu_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单类型（1菜单 2权限 3开发）',
  `menu_href` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接',
  `menu_target` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标',
  `menu_icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `menu_color` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `menu_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `permission` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `weight` decimal(4, 0) NULL DEFAULT NULL COMMENT '菜单权重',
  `is_show` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否显示（1显示 0隐藏）',
  `sys_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '归属系统（default:主导航菜单、mobileApp:APP菜单）',
  `module_codes` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '归属模块（多个用逗号隔开）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `extend_s1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 1',
  `extend_s2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 2',
  `extend_s3` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 3',
  `extend_s4` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 4',
  `extend_s5` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 5',
  `extend_s6` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 6',
  `extend_s7` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 7',
  `extend_s8` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 8',
  `extend_i1` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 1',
  `extend_i2` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 2',
  `extend_i3` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 3',
  `extend_i4` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 4',
  `extend_f1` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 1',
  `extend_f2` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 2',
  `extend_f3` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 3',
  `extend_f4` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 4',
  `extend_d1` datetime NULL DEFAULT NULL COMMENT '扩展 Date 1',
  `extend_d2` datetime NULL DEFAULT NULL COMMENT '扩展 Date 2',
  `extend_d3` datetime NULL DEFAULT NULL COMMENT '扩展 Date 3',
  `extend_d4` datetime NULL DEFAULT NULL COMMENT '扩展 Date 4',
  PRIMARY KEY (`menu_code`) USING BTREE,
  INDEX `idx_sys_menu_pc`(`parent_code`) USING BTREE,
  INDEX `idx_sys_menu_ts`(`tree_sort`) USING BTREE,
  INDEX `idx_sys_menu_status`(`status`) USING BTREE,
  INDEX `idx_sys_menu_mt`(`menu_type`) USING BTREE,
  INDEX `idx_sys_menu_pss`(`parent_codes`) USING BTREE,
  INDEX `idx_sys_menu_tss`(`tree_sorts`) USING BTREE,
  INDEX `idx_sys_menu_sc`(`sys_code`) USING BTREE,
  INDEX `idx_sys_menu_is`(`is_show`) USING BTREE,
  INDEX `idx_sys_menu_mcs`(`module_codes`) USING BTREE,
  INDEX `idx_sys_menu_wt`(`weight`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_module
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_module`;
CREATE TABLE `js_sys_module`  (
  `module_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块编码',
  `module_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块名称',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块描述',
  `main_class_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主类全名',
  `current_version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前版本',
  `upgrade_info` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '升级信息',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`module_code`) USING BTREE,
  INDEX `idx_sys_module_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模块表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_msg_inner
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_msg_inner`;
CREATE TABLE `js_sys_msg_inner`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `msg_title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息标题',
  `content_level` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容级别（1普通 2一般 3紧急）',
  `content_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容类型（1公告 2新闻 3会议 4其它）',
  `msg_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息内容',
  `receive_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者类型（0全部 1用户 2部门 3角色 4岗位）',
  `receive_codes` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '接受者字符串',
  `receive_names` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '接受者名称字符串',
  `send_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送者用户编码',
  `send_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送者用户姓名',
  `send_date` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `is_attac` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否有附件',
  `notify_types` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知类型（PC APP 短信 邮件 微信）多选',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 4审核 5驳回 9草稿）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_msg_inner_cb`(`create_by`) USING BTREE,
  INDEX `idx_sys_msg_inner_status`(`status`) USING BTREE,
  INDEX `idx_sys_msg_inner_cl`(`content_level`) USING BTREE,
  INDEX `idx_sys_msg_inner_sc`(`send_user_code`) USING BTREE,
  INDEX `idx_sys_msg_inner_sd`(`send_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内部消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_msg_inner_record
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_msg_inner_record`;
CREATE TABLE `js_sys_msg_inner_record`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `msg_inner_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属消息',
  `receive_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者用户编码',
  `receive_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者用户姓名',
  `read_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '读取状态（0未送达 1已读 2未读）',
  `read_date` datetime NULL DEFAULT NULL COMMENT '阅读时间',
  `is_star` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否标星',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_msg_inner_r_mi`(`msg_inner_id`) USING BTREE,
  INDEX `idx_sys_msg_inner_r_ruc`(`receive_user_code`) USING BTREE,
  INDEX `idx_sys_msg_inner_r_stat`(`read_status`) USING BTREE,
  INDEX `idx_sys_msg_inner_r_star`(`is_star`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内部消息发送记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_msg_push
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_msg_push`;
CREATE TABLE `js_sys_msg_push`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `msg_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型（PC APP 短信 邮件 微信）',
  `msg_title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息标题',
  `msg_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息内容',
  `biz_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `biz_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `receive_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者账号',
  `receive_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者用户编码',
  `receive_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者用户姓名',
  `send_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送者用户编码',
  `send_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送者用户姓名',
  `send_date` datetime NOT NULL COMMENT '发送时间',
  `is_merge_push` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否合并推送',
  `plan_push_date` datetime NULL DEFAULT NULL COMMENT '计划推送时间',
  `push_number` int(11) NULL DEFAULT NULL COMMENT '推送尝试次数',
  `push_return_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送返回结果码',
  `push_return_msg_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送返回消息编号',
  `push_return_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '推送返回的内容信息',
  `push_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送状态（0未推送 1成功  2失败）',
  `push_date` datetime NULL DEFAULT NULL COMMENT '推送时间',
  `read_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读取状态（0未送达 1已读 2未读）',
  `read_date` datetime NULL DEFAULT NULL COMMENT '读取时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_msg_push_type`(`msg_type`) USING BTREE,
  INDEX `idx_sys_msg_push_rc`(`receive_code`) USING BTREE,
  INDEX `idx_sys_msg_push_uc`(`receive_user_code`) USING BTREE,
  INDEX `idx_sys_msg_push_suc`(`send_user_code`) USING BTREE,
  INDEX `idx_sys_msg_push_pd`(`plan_push_date`) USING BTREE,
  INDEX `idx_sys_msg_push_ps`(`push_status`) USING BTREE,
  INDEX `idx_sys_msg_push_rs`(`read_status`) USING BTREE,
  INDEX `idx_sys_msg_push_bk`(`biz_key`) USING BTREE,
  INDEX `idx_sys_msg_push_bt`(`biz_type`) USING BTREE,
  INDEX `idx_sys_msg_push_imp`(`is_merge_push`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息推送表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_msg_pushed
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_msg_pushed`;
CREATE TABLE `js_sys_msg_pushed`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `msg_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型（PC APP 短信 邮件 微信）',
  `msg_title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息标题',
  `msg_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息内容',
  `biz_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `biz_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `receive_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者账号',
  `receive_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者用户编码',
  `receive_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接受者用户姓名',
  `send_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送者用户编码',
  `send_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送者用户姓名',
  `send_date` datetime NOT NULL COMMENT '发送时间',
  `is_merge_push` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否合并推送',
  `plan_push_date` datetime NULL DEFAULT NULL COMMENT '计划推送时间',
  `push_number` int(11) NULL DEFAULT NULL COMMENT '推送尝试次数',
  `push_return_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '推送返回的内容信息',
  `push_return_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送返回结果码',
  `push_return_msg_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送返回消息编号',
  `push_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送状态（0未推送 1成功  2失败）',
  `push_date` datetime NULL DEFAULT NULL COMMENT '推送时间',
  `read_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读取状态（0未送达 1已读 2未读）',
  `read_date` datetime NULL DEFAULT NULL COMMENT '读取时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_msg_pushed_type`(`msg_type`) USING BTREE,
  INDEX `idx_sys_msg_pushed_rc`(`receive_code`) USING BTREE,
  INDEX `idx_sys_msg_pushed_uc`(`receive_user_code`) USING BTREE,
  INDEX `idx_sys_msg_pushed_suc`(`send_user_code`) USING BTREE,
  INDEX `idx_sys_msg_pushed_pd`(`plan_push_date`) USING BTREE,
  INDEX `idx_sys_msg_pushed_ps`(`push_status`) USING BTREE,
  INDEX `idx_sys_msg_pushed_rs`(`read_status`) USING BTREE,
  INDEX `idx_sys_msg_pushed_bk`(`biz_key`) USING BTREE,
  INDEX `idx_sys_msg_pushed_bt`(`biz_type`) USING BTREE,
  INDEX `idx_sys_msg_pushed_imp`(`is_merge_push`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息已推送表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_msg_template
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_msg_template`;
CREATE TABLE `js_sys_msg_template`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `module_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属模块',
  `tpl_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板键值',
  `tpl_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
  `tpl_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板类型',
  `tpl_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板内容',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_msg_tpl_key`(`tpl_key`) USING BTREE,
  INDEX `idx_sys_msg_tpl_type`(`tpl_type`) USING BTREE,
  INDEX `idx_sys_msg_tpl_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_office
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_office`;
CREATE TABLE `js_sys_office`  (
  `office_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构编码',
  `parent_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10, 0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_sorts` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有级别排序号',
  `tree_leaf` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否最末级',
  `tree_level` decimal(4, 0) NOT NULL COMMENT '层次级别',
  `tree_names` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全节点名',
  `view_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构代码',
  `office_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构名称',
  `full_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构全称',
  `office_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构类型',
  `leader` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '办公电话',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `email` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `corp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '租户代码',
  `corp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSite' COMMENT '租户名称',
  `extend_s1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 1',
  `extend_s2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 2',
  `extend_s3` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 3',
  `extend_s4` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 4',
  `extend_s5` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 5',
  `extend_s6` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 6',
  `extend_s7` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 7',
  `extend_s8` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 8',
  `extend_i1` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 1',
  `extend_i2` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 2',
  `extend_i3` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 3',
  `extend_i4` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 4',
  `extend_f1` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 1',
  `extend_f2` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 2',
  `extend_f3` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 3',
  `extend_f4` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 4',
  `extend_d1` datetime NULL DEFAULT NULL COMMENT '扩展 Date 1',
  `extend_d2` datetime NULL DEFAULT NULL COMMENT '扩展 Date 2',
  `extend_d3` datetime NULL DEFAULT NULL COMMENT '扩展 Date 3',
  `extend_d4` datetime NULL DEFAULT NULL COMMENT '扩展 Date 4',
  PRIMARY KEY (`office_code`) USING BTREE,
  INDEX `idx_sys_office_cc`(`corp_code`) USING BTREE,
  INDEX `idx_sys_office_pc`(`parent_code`) USING BTREE,
  INDEX `idx_sys_office_pcs`(`parent_codes`) USING BTREE,
  INDEX `idx_sys_office_status`(`status`) USING BTREE,
  INDEX `idx_sys_office_ot`(`office_type`) USING BTREE,
  INDEX `idx_sys_office_vc`(`view_code`) USING BTREE,
  INDEX `idx_sys_office_ts`(`tree_sort`) USING BTREE,
  INDEX `idx_sys_office_tss`(`tree_sorts`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织机构表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_post
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_post`;
CREATE TABLE `js_sys_post`  (
  `post_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
  `post_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位分类（高管、中层、基层）',
  `post_sort` decimal(10, 0) NULL DEFAULT NULL COMMENT '岗位排序（升序）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `corp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '租户代码',
  `corp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSite' COMMENT '租户名称',
  PRIMARY KEY (`post_code`) USING BTREE,
  INDEX `idx_sys_post_cc`(`corp_code`) USING BTREE,
  INDEX `idx_sys_post_status`(`status`) USING BTREE,
  INDEX `idx_sys_post_ps`(`post_sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '员工岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_role`;
CREATE TABLE `js_sys_role`  (
  `role_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色分类（高管、中层、基层、其它）',
  `role_sort` decimal(10, 0) NULL DEFAULT NULL COMMENT '角色排序（升序）',
  `is_sys` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统内置（1是 0否）',
  `user_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户类型（employee员工 member会员）',
  `data_scope` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据范围设置（0未设置  1全部数据 2自定义数据）',
  `biz_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '适应业务范围（不同的功能，不同的数据权限支持）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `corp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '租户代码',
  `corp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSite' COMMENT '租户名称',
  `extend_s1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 1',
  `extend_s2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 2',
  `extend_s3` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 3',
  `extend_s4` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 4',
  `extend_s5` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 5',
  `extend_s6` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 6',
  `extend_s7` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 7',
  `extend_s8` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 8',
  `extend_i1` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 1',
  `extend_i2` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 2',
  `extend_i3` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 3',
  `extend_i4` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 4',
  `extend_f1` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 1',
  `extend_f2` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 2',
  `extend_f3` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 3',
  `extend_f4` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 4',
  `extend_d1` datetime NULL DEFAULT NULL COMMENT '扩展 Date 1',
  `extend_d2` datetime NULL DEFAULT NULL COMMENT '扩展 Date 2',
  `extend_d3` datetime NULL DEFAULT NULL COMMENT '扩展 Date 3',
  `extend_d4` datetime NULL DEFAULT NULL COMMENT '扩展 Date 4',
  PRIMARY KEY (`role_code`) USING BTREE,
  INDEX `idx_sys_role_cc`(`corp_code`) USING BTREE,
  INDEX `idx_sys_role_is`(`is_sys`) USING BTREE,
  INDEX `idx_sys_role_status`(`status`) USING BTREE,
  INDEX `idx_sys_role_rs`(`role_sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_role_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_role_data_scope`;
CREATE TABLE `js_sys_role_data_scope`  (
  `role_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制角色编码',
  `ctrl_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制类型',
  `ctrl_data` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制数据',
  `ctrl_permi` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制权限',
  PRIMARY KEY (`role_code`, `ctrl_type`, `ctrl_data`, `ctrl_permi`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色数据权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_role_menu`;
CREATE TABLE `js_sys_role_menu`  (
  `role_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `menu_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单编码',
  PRIMARY KEY (`role_code`, `menu_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_user`;
CREATE TABLE `js_sys_user`  (
  `user_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编码',
  `login_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `email` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '办公电话',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户性别',
  `avatar` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像路径',
  `sign` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `wx_openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定的微信号',
  `mobile_imei` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定的手机串号',
  `user_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户类型',
  `ref_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户类型引用编号',
  `ref_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户类型引用姓名',
  `mgr_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员类型（0非管理员 1系统管理员  2二级管理员）',
  `pwd_security_level` decimal(1, 0) NULL DEFAULT NULL COMMENT '密码安全级别（0初始 1很弱 2弱 3安全 4很安全）',
  `pwd_update_date` datetime NULL DEFAULT NULL COMMENT '密码最后更新时间',
  `pwd_update_record` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码修改记录',
  `pwd_question` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密保问题',
  `pwd_question_answer` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密保问题答案',
  `pwd_question_2` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密保问题2',
  `pwd_question_answer_2` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密保问题答案2',
  `pwd_question_3` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密保问题3',
  `pwd_question_answer_3` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密保问题答案3',
  `pwd_quest_update_date` datetime NULL DEFAULT NULL COMMENT '密码问题修改时间',
  `last_login_ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登陆IP',
  `last_login_date` datetime NULL DEFAULT NULL COMMENT '最后登陆时间',
  `freeze_date` datetime NULL DEFAULT NULL COMMENT '冻结时间',
  `freeze_cause` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冻结原因',
  `user_weight` decimal(8, 0) NULL DEFAULT 0 COMMENT '用户权重（降序）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用 3冻结）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `corp_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '租户代码',
  `corp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'JeeSite' COMMENT '租户名称',
  `extend_s1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 1',
  `extend_s2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 2',
  `extend_s3` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 3',
  `extend_s4` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 4',
  `extend_s5` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 5',
  `extend_s6` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 6',
  `extend_s7` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 7',
  `extend_s8` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展 String 8',
  `extend_i1` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 1',
  `extend_i2` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 2',
  `extend_i3` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 3',
  `extend_i4` decimal(19, 0) NULL DEFAULT NULL COMMENT '扩展 Integer 4',
  `extend_f1` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 1',
  `extend_f2` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 2',
  `extend_f3` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 3',
  `extend_f4` decimal(19, 4) NULL DEFAULT NULL COMMENT '扩展 Float 4',
  `extend_d1` datetime NULL DEFAULT NULL COMMENT '扩展 Date 1',
  `extend_d2` datetime NULL DEFAULT NULL COMMENT '扩展 Date 2',
  `extend_d3` datetime NULL DEFAULT NULL COMMENT '扩展 Date 3',
  `extend_d4` datetime NULL DEFAULT NULL COMMENT '扩展 Date 4',
  PRIMARY KEY (`user_code`) USING BTREE,
  INDEX `idx_sys_user_lc`(`login_code`) USING BTREE,
  INDEX `idx_sys_user_email`(`email`) USING BTREE,
  INDEX `idx_sys_user_mobile`(`mobile`) USING BTREE,
  INDEX `idx_sys_user_wo`(`wx_openid`) USING BTREE,
  INDEX `idx_sys_user_imei`(`mobile_imei`) USING BTREE,
  INDEX `idx_sys_user_rt`(`user_type`) USING BTREE,
  INDEX `idx_sys_user_rc`(`ref_code`) USING BTREE,
  INDEX `idx_sys_user_mt`(`mgr_type`) USING BTREE,
  INDEX `idx_sys_user_us`(`user_weight`) USING BTREE,
  INDEX `idx_sys_user_ud`(`update_date`) USING BTREE,
  INDEX `idx_sys_user_status`(`status`) USING BTREE,
  INDEX `idx_sys_user_cc`(`corp_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_user_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_user_data_scope`;
CREATE TABLE `js_sys_user_data_scope`  (
  `user_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制用户编码',
  `ctrl_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制类型',
  `ctrl_data` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制数据',
  `ctrl_permi` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制权限',
  PRIMARY KEY (`user_code`, `ctrl_type`, `ctrl_data`, `ctrl_permi`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户数据权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for js_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `js_sys_user_role`;
CREATE TABLE `js_sys_user_role`  (
  `user_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编码',
  `role_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  PRIMARY KEY (`user_code`, `role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户与角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for line
-- ----------------------------
DROP TABLE IF EXISTS `line`;
CREATE TABLE `line`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小组id',
  `line_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线名称',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `小组ID`(`group_id`) USING BTREE,
  CONSTRAINT `line_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `group_management` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '导线' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for observation_data
-- ----------------------------
DROP TABLE IF EXISTS `observation_data`;
CREATE TABLE `observation_data`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '测站id',
  `fore_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '前视站点id',
  `back_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '后视站点id',
  `observation_round` int(11) NOT NULL COMMENT '测回',
  `fore_left_deg` int(11) NOT NULL COMMENT '前左度',
  `fore_left_min` int(11) NOT NULL COMMENT '前左分',
  `fore_left_sec` double(3, 1) NOT NULL COMMENT '前左秒',
  `fore_right_deg` int(11) NOT NULL COMMENT '前右度',
  `fore_right_min` int(11) NOT NULL COMMENT '前右分',
  `fore_right_sec` double(3, 1) NOT NULL COMMENT '前右秒',
  `back_left_deg` int(11) NOT NULL COMMENT '后左度',
  `back_left_min` int(11) NOT NULL COMMENT '后左分',
  `back_left_sec` double(3, 1) NOT NULL COMMENT '后左秒',
  `back_right_deg` int(11) NOT NULL COMMENT '后右度',
  `back_right_min` int(11) NOT NULL COMMENT '后右分',
  `back_right_sec` double(3, 1) NOT NULL COMMENT '后右秒',
  `fore_distance` double(8, 4) NOT NULL COMMENT '前视平距(m)',
  `back_distance` double(8, 4) NOT NULL COMMENT '后视平距(m)',
  `half_left_deg` int(11) NULL DEFAULT NULL COMMENT '盘左半测回角值度',
  `half_left_min` int(11) NULL DEFAULT NULL COMMENT '盘左半测回角值分',
  `half_left_sec` double(3, 1) NULL DEFAULT NULL COMMENT '盘左半测回角值秒',
  `half_right_deg` int(11) NULL DEFAULT NULL COMMENT '盘右半测回角值度',
  `half_right_min` int(11) NULL DEFAULT NULL COMMENT '盘右半测回角值分',
  `half_right_sec` double(3, 1) NULL DEFAULT NULL COMMENT '盘右半测回角值秒',
  `full_value_deg` int(11) NULL DEFAULT NULL COMMENT '一测回角值度',
  `full_value_min` int(11) NULL DEFAULT NULL COMMENT '一测回角值分',
  `full_value_sec` double(3, 1) NULL DEFAULT NULL COMMENT '一测回角值秒',
  `turning_ang_radian` double(10, 9) NULL DEFAULT NULL COMMENT '转折角弧度',
  `double_c_fore` double NULL DEFAULT NULL COMMENT '前视2C互差',
  `double_c_back` double NULL DEFAULT NULL COMMENT '后视2C互差',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `观测数据的测站ID`(`station_id`) USING BTREE,
  INDEX `fore_stn_id`(`fore_stn_id`) USING BTREE,
  INDEX `back_stn_id`(`back_stn_id`) USING BTREE,
  CONSTRAINT `observation_data_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '观测数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for origin_data
-- ----------------------------
DROP TABLE IF EXISTS `origin_data`;
CREATE TABLE `origin_data`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `origin_station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '起始测站id',
  `origin_azimuth_deg` int(11) NOT NULL COMMENT '起始方位角度',
  `origin_azimuth_min` int(11) NOT NULL COMMENT '起始方位角分',
  `origin_azimuth_sec` double(3, 1) NOT NULL COMMENT '起始方位角秒',
  `origin_coord_x` decimal(20, 4) NOT NULL COMMENT '起始坐标X',
  `origin_coord_y` decimal(20, 4) NOT NULL COMMENT '起始坐标Y',
  `end_station_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '终点测站id',
  `end_azimuth_deg` int(11) NOT NULL COMMENT '终点方位角度',
  `end_azimuth_min` int(11) NOT NULL COMMENT '终点方位角分',
  `end_azimuth_sec` double(3, 1) NOT NULL COMMENT '终点方位角秒',
  `end_coord_x` decimal(20, 4) NOT NULL COMMENT '终点坐标X',
  `end_coord_y` decimal(20, 4) NOT NULL COMMENT '终点坐标Y',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_module_status`(`status`) USING BTREE,
  INDEX `测站ID`(`origin_station_id`) USING BTREE,
  INDEX `end_station_id`(`end_station_id`) USING BTREE,
  CONSTRAINT `origin_data_ibfk_1` FOREIGN KEY (`origin_station_id`) REFERENCES `station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `origin_data_ibfk_2` FOREIGN KEY (`end_station_id`) REFERENCES `station` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '起始数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for result_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `result_evaluation`;
CREATE TABLE `result_evaluation`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `line_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线id',
  `angle_closure_error` double(30, 4) NULL DEFAULT NULL COMMENT '角度闭合差（秒）',
  `clos_err_of_coordincreX` double(30, 4) NULL DEFAULT NULL COMMENT '坐标增量闭合差X（毫米）',
  `clos_err_of_coordincreY` double(30, 4) NULL DEFAULT NULL COMMENT '坐标增量闭合差Y（毫米）',
  `total_length_clos_error` double(30, 6) NULL DEFAULT NULL COMMENT '导线全长闭合差（毫米）',
  `Rela_clos_error` double(30, 20) NULL DEFAULT NULL COMMENT '导线相对闭合差',
  `traverse_class` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导线等级（三等、四等、一级、二级、三级）',
  `result_evaluation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果评价（√ or 不通过原因）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `结算结果的导线ID`(`line_id`) USING BTREE,
  CONSTRAINT `result_evaluation_ibfk_1` FOREIGN KEY (`line_id`) REFERENCES `line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '结果评定' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for result_evaluation_strict
-- ----------------------------
DROP TABLE IF EXISTS `result_evaluation_strict`;
CREATE TABLE `result_evaluation_strict`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `line_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线id',
  `mediumError_unitWeight` double(30, 4) NULL DEFAULT NULL COMMENT '单位权中误差（毫米）',
  `traverse_class` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导线等级（三等、四等、一级、二级、三级）',
  `result_evaluation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果评价（√ or 不通过原因）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `solution_date` datetime NULL DEFAULT NULL COMMENT '解算时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `结算结果的导线ID`(`line_id`) USING BTREE,
  CONSTRAINT `result_evaluation_strict_ibfk_1` FOREIGN KEY (`line_id`) REFERENCES `line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '间接平差的结果评定' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for station
-- ----------------------------
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `line_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线id',
  `station_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '测站名称',
  `fore_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前视站点id',
  `back_stn_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后视站点id',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `导线圈ID`(`line_id`) USING BTREE,
  CONSTRAINT `station_ibfk_1` FOREIGN KEY (`line_id`) REFERENCES `line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测站' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for test_data
-- ----------------------------
DROP TABLE IF EXISTS `test_data`;
CREATE TABLE `test_data`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `test_input` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单行文本',
  `test_textarea` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '多行文本',
  `test_select` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下拉框',
  `test_select_multiple` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下拉多选',
  `test_radio` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单选框',
  `test_checkbox` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '复选框',
  `test_date` datetime NULL DEFAULT NULL COMMENT '日期选择',
  `test_datetime` datetime NULL DEFAULT NULL COMMENT '日期时间',
  `test_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户选择',
  `test_office_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构选择',
  `test_area_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域选择',
  `test_area_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测试数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for test_data_child
-- ----------------------------
DROP TABLE IF EXISTS `test_data_child`;
CREATE TABLE `test_data_child`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `test_sort` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `test_data_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父表主键',
  `test_input` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单行文本',
  `test_textarea` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '多行文本',
  `test_select` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下拉框',
  `test_select_multiple` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下拉多选',
  `test_radio` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单选框',
  `test_checkbox` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '复选框',
  `test_date` datetime NULL DEFAULT NULL COMMENT '日期选择',
  `test_datetime` datetime NULL DEFAULT NULL COMMENT '日期时间',
  `test_user_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户选择',
  `test_office_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构选择',
  `test_area_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域选择',
  `test_area_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测试数据子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for test_tree
-- ----------------------------
DROP TABLE IF EXISTS `test_tree`;
CREATE TABLE `test_tree`  (
  `tree_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点编码',
  `parent_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10, 0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_sorts` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所有级别排序号',
  `tree_leaf` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否最末级',
  `tree_level` decimal(4, 0) NOT NULL COMMENT '层次级别',
  `tree_names` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全节点名',
  `tree_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点名称',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`tree_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测试树表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wrong_result
-- ----------------------------
DROP TABLE IF EXISTS `wrong_result`;
CREATE TABLE `wrong_result`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小组id',
  `line_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '导线名称',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `wrong` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误位置',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `wrong_result_ibfk_1`(`group_id`) USING BTREE,
  INDEX `wrong_result_ibfk_2`(`line_id`) USING BTREE,
  CONSTRAINT `wrong_result_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `group_management` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `wrong_result_ibfk_2` FOREIGN KEY (`line_id`) REFERENCES `line` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '错误边角查询' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
