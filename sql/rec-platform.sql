/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : rec-platform

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 16/11/2022 09:57:31
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for co_chapter
-- ----------------------------
DROP TABLE IF EXISTS `co_chapter`;
CREATE TABLE `co_chapter`  (
  `id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '章节id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '章节名称',
  `pid` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父章节id    首级章节设置默认值',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `chapter_order` smallint UNSIGNED NOT NULL COMMENT '章节顺序    小数靠前',
  `has_coursetime` tinyint UNSIGNED NOT NULL COMMENT '是否存在课时',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除     1-删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_chapter
-- ----------------------------
INSERT INTO `co_chapter` VALUES ('1565157575404511234', '第一章', '-1', '1518868937486368710', 1, 0, 0, 0, '2022-09-01 10:00:20', '2022-09-01 10:00:20');
INSERT INTO `co_chapter` VALUES ('1565158584860237825', '第二章', '-1', '1518868937486368710', 1, 0, 0, 0, '2022-09-01 10:04:21', '2022-09-01 10:04:21');
INSERT INTO `co_chapter` VALUES ('1565159027661299714', '第一章第一节', '1565157575404511234', '1518868937486368710', 1, 1, 0, 0, '2022-09-01 10:06:06', '2022-09-01 10:06:06');
INSERT INTO `co_chapter` VALUES ('1566620279604486146', '第一章第二节', '1565157575404511234', '1518868937486368710', 2, 0, 0, 0, '2022-09-05 10:52:36', '2022-09-05 10:52:36');
INSERT INTO `co_chapter` VALUES ('1569524537177927681', '第一章第3节', '1565157575404511234', '1518868937486368710', 3, 0, 4, 0, '2022-09-13 11:13:05', '2022-09-18 10:33:55');

-- ----------------------------
-- Table structure for co_class
-- ----------------------------
DROP TABLE IF EXISTS `co_class`;
CREATE TABLE `co_class`  (
  `class_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级id',
  `class_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级名',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `student_number` smallint UNSIGNED NOT NULL COMMENT '班级学生总数',
  `teacher_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教师id（创建班级的用户就是教师）',
  `class_invitation_code` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级邀请码',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '班级-课程表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_class
-- ----------------------------
INSERT INTO `co_class` VALUES ('1548613711105740801', '19测试1班', '1518868937486368710', 2, '1548612514730786818', '8D1HJ75C', 2, 0, '2022-07-17 18:20:55', '2022-07-17 18:43:49');
INSERT INTO `co_class` VALUES ('1548614267316588545', '默认班级', '1518868937486368710', 1, '1548612514730786818', 'HYN3477Z', 1, 0, '2022-07-17 18:23:08', '2022-10-06 21:06:22');
INSERT INTO `co_class` VALUES ('1569332125188886529', '默认班级', '1569332125172109313', 0, '1540618175123570690', 'G6194MEP', 0, 0, '2022-09-12 22:28:30', '2022-09-12 22:28:30');

-- ----------------------------
-- Table structure for co_class_time
-- ----------------------------
DROP TABLE IF EXISTS `co_class_time`;
CREATE TABLE `co_class_time`  (
  `class_time_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课时id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课时名称',
  `chapter_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '章节id',
  `paper_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业id',
  `paper_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业名称',
  `class_time_order` tinyint UNSIGNED NOT NULL COMMENT '课时顺序   数小靠前',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除  1-删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`class_time_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '课时表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_class_time
-- ----------------------------
INSERT INTO `co_class_time` VALUES ('1569517725686939650', '课时1', '1565159027661299714', '1559402969504415746', '作业1', 1, 0, 0, '2022-09-13 10:46:01', '2022-09-13 10:46:01');
INSERT INTO `co_class_time` VALUES ('1569518459811749890', '课时2', '1565159027661299714', '1559402969504415746', '作业2', 2, 0, 0, '2022-09-13 10:48:56', '2022-09-13 10:48:56');
INSERT INTO `co_class_time` VALUES ('1569519208348213250', '课时3', '1565159027661299714', '1559402969504415746', '作业3', 3, 0, 0, '2022-09-13 10:51:54', '2022-09-13 10:51:54');

-- ----------------------------
-- Table structure for co_class_time_resource
-- ----------------------------
DROP TABLE IF EXISTS `co_class_time_resource`;
CREATE TABLE `co_class_time_resource`  (
  `time_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课时id',
  `resource_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源id',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除 ',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_class_time_resource
-- ----------------------------
INSERT INTO `co_class_time_resource` VALUES ('1569517725686939650', '1569686989551255554', 0, 0, '2022-09-13 10:46:01', '2022-09-13 10:46:01');
INSERT INTO `co_class_time_resource` VALUES ('1569517725686939650', '1569683268209594369', 0, 0, '2022-09-13 10:46:01', '2022-09-13 10:46:01');
INSERT INTO `co_class_time_resource` VALUES ('1569518459811749890', '1569683268209594369', 0, 0, '2022-09-13 10:51:18', '2022-09-13 10:51:18');
INSERT INTO `co_class_time_resource` VALUES ('1569519208348213250', '1569683268209594369', 0, 0, '2022-09-13 10:51:54', '2022-09-13 10:51:54');
INSERT INTO `co_class_time_resource` VALUES ('1569519208348213250', '1569683268209594369', 0, 0, '2022-09-13 10:51:54', '2022-09-13 10:51:54');

-- ----------------------------
-- Table structure for co_class_user
-- ----------------------------
DROP TABLE IF EXISTS `co_class_user`;
CREATE TABLE `co_class_user`  (
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `class_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级id',
  `role` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '用户角色（默认0普通学生）',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_class_user
-- ----------------------------
INSERT INTO `co_class_user` VALUES ('1523918834958868482', '1548613711105740801', 0, 0, 0, '2022-07-17 18:42:28', '2022-07-17 18:42:28');
INSERT INTO `co_class_user` VALUES ('1540618175123570690', '1548613711105740801', 0, 0, 0, '2022-07-17 18:43:49', '2022-07-17 18:43:49');
INSERT INTO `co_class_user` VALUES ('1523918834958868482', '1548614267316588545', 0, 0, 0, '2022-10-06 21:06:22', '2022-10-06 21:06:22');

-- ----------------------------
-- Table structure for co_course
-- ----------------------------
DROP TABLE IF EXISTS `co_course`;
CREATE TABLE `co_course`  (
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `course_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程名',
  `course_cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程封面路径',
  `course_describe` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程描述',
  `course_builder_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程创建者id',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`course_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '课程详情表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_course
-- ----------------------------
INSERT INTO `co_course` VALUES ('1518868937486368710', '离散数学', 'http://edptrqh.it/tdp', '由XXx老师授课', '1523918834958868482', 0, 0, '2022-05-16 14:57:28', '2022-05-11 14:57:31');
INSERT INTO `co_course` VALUES ('1569332125172109313', '测试', 'http://edptrqh.it/tdp', 'dolore ut adipisicing eu in', '1540618175123570690', 1, 0, '2022-09-12 22:28:30', '2022-09-12 22:29:52');

-- ----------------------------
-- Table structure for co_point
-- ----------------------------
DROP TABLE IF EXISTS `co_point`;
CREATE TABLE `co_point`  (
  `id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '知识点id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '知识点名称',
  `pid` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父知识点id',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `version` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '乐观锁',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_point
-- ----------------------------
INSERT INTO `co_point` VALUES ('1548237301487091714', '第一章', '555', '1518868937486368710', 0, 0, '2022-10-12 21:18:03', '2022-10-10 21:18:07');
INSERT INTO `co_point` VALUES ('1548237344227049473', '第二章', '555', '1518868937486368710', 0, 0, '2022-10-03 21:18:51', '2022-10-03 21:18:55');
INSERT INTO `co_point` VALUES ('1548237376028262402', '命题', '1548237301487091714', '1518868937486368710', 0, 0, '2022-07-16 17:25:30', '2022-07-16 17:25:30');
INSERT INTO `co_point` VALUES ('1548237398266462210', '真命题', '1548237301487091714', '1518868937486368710', 0, 0, '2022-07-16 17:25:35', '2022-07-16 17:25:35');
INSERT INTO `co_point` VALUES ('1558262141767667713', '假命题', '1548237301487091714', '1518868937486368710', 0, 0, '2022-08-13 09:20:20', '2022-08-13 09:20:20');
INSERT INTO `co_point` VALUES ('1558262788751642625', '命题规则', '1548237301487091714', '1518868937486368710', 0, 0, '2022-08-13 09:22:55', '2022-08-13 09:22:55');
INSERT INTO `co_point` VALUES ('1558264003279474690', '一范式', '1548237344227049473', '1518868937486368710', 0, 0, '2022-08-13 09:27:44', '2022-08-13 09:27:44');
INSERT INTO `co_point` VALUES ('1558264044052303874', '二范式', '1548237344227049473', '1518868937486368710', 0, 0, '2022-08-13 09:27:54', '2022-08-13 09:27:54');
INSERT INTO `co_point` VALUES ('1581845276962811905', '测试', '67', '11', 0, 0, '2022-10-17 11:11:18', '2022-10-17 11:11:18');

-- ----------------------------
-- Table structure for co_point_relation
-- ----------------------------
DROP TABLE IF EXISTS `co_point_relation`;
CREATE TABLE `co_point_relation`  (
  `point_a_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'a知识点id',
  `point_b_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'b知识点id',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程id',
  `relation` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0表示前后序关系，1表示包含关系，暂时作废',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_point_relation
-- ----------------------------
INSERT INTO `co_point_relation` VALUES ('1548237376028262402', '1548237398266462210', '1518868937486368710', 0, 0, 0, '2022-08-13 10:08:59', '2022-08-13 10:08:59');
INSERT INTO `co_point_relation` VALUES ('1548237376028262402', '1558262141767667713', '1518868937486368710', 0, 0, 0, '2022-08-13 10:08:59', '2022-08-13 10:08:59');
INSERT INTO `co_point_relation` VALUES ('1558262141767667713', '1558262788751642625', '1518868937486368710', 0, 0, 0, '2022-08-13 10:11:31', '2022-08-13 10:11:31');
INSERT INTO `co_point_relation` VALUES ('1558264003279474690', '1558264044052303874', '1518868937486368710', 0, 0, 0, '2022-08-13 10:11:31', '2022-08-13 10:11:31');
INSERT INTO `co_point_relation` VALUES ('1548237301487091714', '1548237376028262402', '1518868937486368710', 0, 0, 0, '2022-09-27 11:26:49', '2022-10-19 11:26:52');
INSERT INTO `co_point_relation` VALUES ('1548237301487091714', '1548237398266462210', '1518868937486368710', 0, 0, 0, '2022-10-19 11:27:28', '2022-10-19 11:27:30');
INSERT INTO `co_point_relation` VALUES ('1548237301487091714', '1558262141767667713', '1518868937486368710', 0, 0, 0, '2022-10-19 11:28:14', '2022-10-19 11:28:18');
INSERT INTO `co_point_relation` VALUES ('1548237301487091714', '1558262788751642625', '1518868937486368710', 0, 0, 0, '2022-10-19 11:28:44', '2022-10-19 11:28:47');
INSERT INTO `co_point_relation` VALUES ('1548237344227049473', '1558264003279474690', '1518868937486368710', 0, 0, 0, '2022-10-19 11:29:15', '2022-10-19 11:29:18');
INSERT INTO `co_point_relation` VALUES ('1548237344227049473', '1558264044052303874', '1518868937486368710', 0, 0, 0, '2022-10-19 11:29:48', '2022-10-19 11:29:51');

-- ----------------------------
-- Table structure for co_resource
-- ----------------------------
DROP TABLE IF EXISTS `co_resource`;
CREATE TABLE `co_resource`  (
  `resource_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源id',
  `resource_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程id（资源所属课程）',
  `type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '资源类型',
  `resource_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源链接',
  `resource_uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源uuid(删除使用)，视频的话存阿里云视频id',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '更新时间',
  `update_time` datetime NOT NULL COMMENT '删除时间',
  PRIMARY KEY (`resource_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_resource
-- ----------------------------
INSERT INTO `co_resource` VALUES ('1567169348777238530', '头像.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/8cb6fa5e-1d6c-4e10-9头像.jpg', '8cb6fa5e-1d6c-4e10-9', 0, 0, '2022-09-06 23:14:24', '2022-09-06 23:14:24');
INSERT INTO `co_resource` VALUES ('1567169425449115649', '头像.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/fe8a6469-66d4-4827-a头像.jpg', 'fe8a6469-66d4-4827-a', 0, 0, '2022-09-06 23:14:42', '2022-09-06 23:14:42');
INSERT INTO `co_resource` VALUES ('1567169434655612930', '头像.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/28c14b08-a586-4a6b-b头像.jpg', '28c14b08-a586-4a6b-b', 0, 0, '2022-09-06 23:14:44', '2022-09-06 23:14:44');
INSERT INTO `co_resource` VALUES ('1567169442574458882', '头像.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/859e9b2e-533d-45d0-8头像.jpg', '859e9b2e-533d-45d0-8', 0, 0, '2022-09-06 23:14:46', '2022-09-06 23:14:46');
INSERT INTO `co_resource` VALUES ('1567169485293445122', '雪山.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/dab91729-4ea1-4a68-8雪山.jpg', 'dab91729-4ea1-4a68-8', 0, 0, '2022-09-06 23:14:57', '2022-09-06 23:14:57');
INSERT INTO `co_resource` VALUES ('1567169492809637889', '雪山.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/2d7e8aa0-4307-4878-b雪山.jpg', '2d7e8aa0-4307-4878-b', 0, 0, '2022-09-06 23:14:58', '2022-09-06 23:14:58');
INSERT INTO `co_resource` VALUES ('1568926325823971330', '女孩.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/b9281c05-807a-4190-b女孩.jpg', 'b9281c05-807a-4190-b', 0, 0, '2022-09-11 19:36:00', '2022-09-11 19:36:00');
INSERT INTO `co_resource` VALUES ('1569683268209594369', 'PPT等分圆.pptx', '1518868937486368710', 20, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/04e5e50c-2cf9-4128-bPPT等分圆.pptx', '04e5e50c-2cf9-4128-b', 0, 0, '2022-09-13 21:43:49', '2022-09-13 21:43:49');
INSERT INTO `co_resource` VALUES ('1569683591225528321', '3-Ego-Spliting Framework from Non-Overlapping to Overlapping Clusters.pptx', '1518868937486368710', 20, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1547211425930256386/9a9a47ca-b834-4142-93-Ego-Spliting Framework from Non-Overlapping to Overlapping Clusters.pptx', '9a9a47ca-b834-4142-9', 0, 0, '2022-09-13 21:45:06', '2022-09-13 21:45:06');
INSERT INTO `co_resource` VALUES ('1569686989551255554', '6 - What If I Want to Move Faster.mp4', '1518868937486368710', 10, 'https://outin-cbbc44500c8511ed9cfb00163e021072.oss-cn-shenzhen.aliyuncs.com/sv/18f35c86-183372501af/18f35c86-183372501af.mp4?Expires=1663081116&OSSAccessKeyId=LTAI4FocoL6tuCdYhuvug6Ee&Signature=hQZtqo1ZAksBEurmE6dKcGLfFb4%3D', '98817a03bc87443fbfc669140ab94750', 0, 0, '2022-09-13 21:58:36', '2022-09-13 21:58:36');
INSERT INTO `co_resource` VALUES ('1571308981836550146', '女孩.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1518868937486368710/05b5a43d-6cf0-4d25-9女孩.jpg', '05b5a43d-6cf0-4d25-9', 0, 0, '2022-09-18 09:23:49', '2022-09-18 09:23:49');
INSERT INTO `co_resource` VALUES ('1573309229970743298', '四级证书.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1518868937486368710/fc3c38b9-f78a-4b4e-9四级证书.jpg', 'fc3c38b9-f78a-4b4e-9', 0, 0, '2022-09-23 21:52:06', '2022-09-23 21:52:06');
INSERT INTO `co_resource` VALUES ('1576762752527994881', '雪山.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1518868937486368710/a0f22d6c-96e4-463f-9雪山.jpg', 'a0f22d6c-96e4-463f-9', 0, 0, '2022-10-03 10:35:10', '2022-10-03 10:35:10');
INSERT INTO `co_resource` VALUES ('1576764406958604290', '雪山.jpg', '1518868937486368710', 41, 'https://platform-yjj.oss-cn-guangzhou.aliyuncs.com/1518868937486368710/30d04be1-a738-44d3-8雪山.jpg', '30d04be1-a738-44d3-8', 0, 0, '2022-10-03 10:41:44', '2022-10-03 10:41:44');

-- ----------------------------
-- Table structure for co_resource_point
-- ----------------------------
DROP TABLE IF EXISTS `co_resource_point`;
CREATE TABLE `co_resource_point`  (
  `point_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '知识点id',
  `resource_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源id',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '知识点-资源关系表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of co_resource_point
-- ----------------------------
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1567169348777238530', 0, 0, '2022-09-06 23:14:24', '2022-09-06 23:14:24');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1567169425449115649', 0, 0, '2022-09-06 23:14:42', '2022-09-06 23:14:42');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1567169434655612930', 0, 0, '2022-09-06 23:14:44', '2022-09-06 23:14:44');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1567169442574458882', 0, 0, '2022-09-06 23:14:46', '2022-09-06 23:14:46');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1567169485293445122', 0, 0, '2022-09-06 23:14:57', '2022-09-06 23:14:57');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1567169492809637889', 0, 0, '2022-09-06 23:14:58', '2022-09-06 23:14:58');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1567703863190859777', 0, 0, '2022-09-08 10:38:22', '2022-09-08 10:38:22');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1568926325823971330', 0, 0, '2022-09-11 19:36:01', '2022-09-11 19:36:01');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1569683268209594369', 0, 0, '2022-09-13 21:43:49', '2022-09-13 21:43:49');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1569683591225528321', 0, 0, '2022-09-13 21:45:06', '2022-09-13 21:45:06');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1569684352638504961', 0, 0, '2022-09-13 21:48:08', '2022-09-13 21:48:08');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1569685962693398530', 0, 0, '2022-09-13 21:54:32', '2022-09-13 21:54:32');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1569686989551255554', 0, 0, '2022-09-13 21:58:37', '2022-09-13 21:58:37');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1571308981836550146', 0, 0, '2022-09-18 09:23:50', '2022-09-18 09:23:50');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1573309229970743298', 0, 0, '2022-09-23 21:52:06', '2022-09-23 21:52:06');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1576762752527994881', 0, 0, '2022-10-03 10:35:10', '2022-10-03 10:35:10');
INSERT INTO `co_resource_point` VALUES ('1548237376028262402', '1576764406958604290', 0, 0, '2022-10-03 10:41:44', '2022-10-03 10:41:44');

-- ----------------------------
-- Table structure for ex_paper
-- ----------------------------
DROP TABLE IF EXISTS `ex_paper`;
CREATE TABLE `ex_paper`  (
  `paper_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '试卷id',
  `paper_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '试卷名称',
  `paper_type` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '试卷类型 0:试卷 1：作业 2：考试',
  `is_release` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已发布，默认为0未发布，1为已发布',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属课程id',
  `pass_score` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '及格分数',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `limit_time` int UNSIGNED NULL DEFAULT NULL COMMENT '限制考试时间',
  `limit_enter_time` int UNSIGNED NULL DEFAULT NULL COMMENT '限制进入时间',
  `limit_submit_time` int UNSIGNED NULL DEFAULT NULL COMMENT '限制提交时间',
  `is_allow_make_up` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否允许补交，默认0否，1是',
  `is_distinguish_case` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '答案是否区分大小写，默认0否，1是',
  `is_multi_half_score` tinyint NULL DEFAULT NULL COMMENT '是否多选题未选全给一半分， 默认0否， 1是',
  `is_get_high_score` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否取历史最高成绩，默认0否，1是',
  `remake_time` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '重做次数，默认0，不能重做',
  `is_allow_show_paper` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否允许考试后查看试卷，默认0否，1是',
  `is_show_score` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否允许学生考试后查看成绩，默认0否，1是',
  `is_show_rank` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否允许学生考试后查看排名，默认0否，1是',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`paper_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业库' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ex_paper
-- ----------------------------
INSERT INTO `ex_paper` VALUES ('1559401362804965372', '第一次月考', 1, 1, '1518868937486368710', NULL, '2022-10-24 18:28:29', '2032-09-12 01:17:59', NULL, 1000000, 120, 0, 0, 1, 0, 1, 1, 1, 1, 2, 0, '2022-08-16 12:47:12', '2022-08-16 12:58:46');
INSERT INTO `ex_paper` VALUES ('1559401362804965378', '第一次月考', 2, 1, '1518868937486368710', NULL, '1971-06-08 18:28:29', '2016-09-12 01:17:59', NULL, NULL, NULL, 0, 0, 0, 0, 1, 1, 0, 1, 2, 0, '2022-08-16 12:47:12', '2022-08-16 12:58:46');
INSERT INTO `ex_paper` VALUES ('1559401362804965379', '第三次月考', 2, 1, '1518868937486368710', NULL, '1971-06-08 18:28:29', '2016-09-12 01:17:59', NULL, NULL, NULL, 0, 0, 0, 0, 1, 1, 1, 1, 2, 0, '2022-08-16 12:47:12', '2022-08-16 12:58:46');
INSERT INTO `ex_paper` VALUES ('1559402969504415742', '第二次作业', 1, 1, '1518868937486368710', NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, '2022-08-16 12:53:35', '2022-08-16 12:53:35');
INSERT INTO `ex_paper` VALUES ('1559402969504415746', '第一次作业', 1, 0, '1518868937486368710', NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, '2022-08-16 12:53:35', '2022-08-16 12:53:35');
INSERT INTO `ex_paper` VALUES ('1559403124014186497', '第二次试卷', 0, 1, '1518868937486368710', 60, '2004-12-25 17:14:57', '1972-03-22 19:17:11', 100, 10, 10, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, '2022-08-16 12:54:12', '2022-08-16 13:01:53');
INSERT INTO `ex_paper` VALUES ('1559403652140097538', '第3次试卷', 0, 0, '1518868937486368710', NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, '2022-08-16 12:56:18', '2022-08-16 12:56:18');

-- ----------------------------
-- Table structure for ex_paper_student
-- ----------------------------
DROP TABLE IF EXISTS `ex_paper_student`;
CREATE TABLE `ex_paper_student`  (
  `paper_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '试卷id',
  `student_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `is_done` tinyint NULL DEFAULT 0 COMMENT '是否已完成， 默认0为否， 1为是， 2为保存未提交',
  `has_remake_time` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '剩余重做次数',
  `is_review` tinyint NULL DEFAULT 0 COMMENT '是否已批阅， 默认0为否， 1为是',
  `submit_time` bigint NOT NULL COMMENT '提交试卷时的时间戳',
  `paper_score` tinyint NULL DEFAULT NULL COMMENT '得分',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业/考试-学生中间表\r\n选择发布学生' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ex_paper_student
-- ----------------------------
INSERT INTO `ex_paper_student` VALUES ('1559401362804965378', '1521413839969615873', '', NULL, 1, 0, 0, NULL, 0, 0, '2022-08-16 12:57:10', '2022-08-16 12:57:10');
INSERT INTO `ex_paper_student` VALUES ('1559401362804965378', '1523918750166818818', '', NULL, 1, 0, 0, NULL, 0, 0, '2022-08-16 12:57:10', '2022-08-16 12:57:10');
INSERT INTO `ex_paper_student` VALUES ('1559401362804965378', '1521413839969615873', '', NULL, 1, 0, 0, NULL, 0, 0, '2022-08-16 12:58:46', '2022-08-16 12:58:46');
INSERT INTO `ex_paper_student` VALUES ('1559401362804965378', '1523918750166818818', '', NULL, 1, 0, 0, NULL, 0, 0, '2022-08-16 12:58:46', '2022-08-16 12:58:46');
INSERT INTO `ex_paper_student` VALUES ('1559403124014186497', '1521413839969615873', '', NULL, 1, 0, 0, NULL, 0, 0, '2022-08-16 13:01:53', '2022-08-16 13:01:53');
INSERT INTO `ex_paper_student` VALUES ('1559402969504415746', '1559401362804965378', '1518868937486368710', 1, 1, 0, 0, NULL, 0, 0, '2022-10-22 00:03:57', '2022-10-22 00:04:00');
INSERT INTO `ex_paper_student` VALUES ('1559401362804965378', '1540618175123570690', '1518868937486368710', 0, 1, 0, 0, NULL, 0, 0, '2022-10-22 00:03:57', '2022-10-22 00:04:00');
INSERT INTO `ex_paper_student` VALUES ('1559401362804965379', '1540618175123570690', '1518868937486368710', 1, 1, 0, 0, NULL, 0, 0, '2022-10-22 00:03:57', '2022-10-22 00:04:00');
INSERT INTO `ex_paper_student` VALUES ('1559401362804965372', '1540618175123570690', '1518868937486368710', 1, 96, 1, 1667869179289, 30, 13, 0, '2022-10-22 00:03:57', '2022-11-08 08:59:59');
INSERT INTO `ex_paper_student` VALUES ('1559402969504415742', '1540618175123570690', '1518868937486368710', 1, 1, 0, 0, NULL, 0, 0, '2022-10-22 00:03:57', '2022-10-22 00:04:00');

-- ----------------------------
-- Table structure for ex_paper_student_answer
-- ----------------------------
DROP TABLE IF EXISTS `ex_paper_student_answer`;
CREATE TABLE `ex_paper_student_answer`  (
  `paper_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '试卷id',
  `student_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id',
  `submit_time` bigint NOT NULL COMMENT '提交试卷时的时间戳',
  `question_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '题目id',
  `right_answer` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '正确答案',
  `student_answer` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生提交答案',
  `score` tinyint NULL DEFAULT NULL COMMENT '题目得分',
  `is_right` tinyint NULL DEFAULT 0 COMMENT '是否正确， 默认0否， 1是',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ex_paper_student_answer
-- ----------------------------
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667195877393, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-10-31 13:57:57', '2022-10-31 13:57:57');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667195877393, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-10-31 13:57:57', '2022-10-31 13:57:57');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667196063629, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-10-31 14:01:04', '2022-10-31 14:01:04');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667196063629, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-10-31 14:01:04', '2022-10-31 14:01:04');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667196481666, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-10-31 14:08:02', '2022-10-31 14:08:02');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667196481666, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-10-31 14:08:02', '2022-10-31 14:08:02');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667196672336, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-10-31 14:11:12', '2022-10-31 14:11:12');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667196672336, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-10-31 14:11:12', '2022-10-31 14:11:12');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667196842492, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-10-31 14:14:03', '2022-10-31 14:14:03');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667196842492, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-10-31 14:14:03', '2022-10-31 14:14:03');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667832260002, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-11-07 22:44:20', '2022-11-07 22:44:20');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667832260002, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-11-07 22:44:20', '2022-11-07 22:44:20');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667832260002, '1558345740441477122', 'C:3#D:4', 'C:3', NULL, 0, 0, 0, '2022-11-07 22:44:20', '2022-11-07 22:44:20');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667832260002, '1558346432061235201', '41', 'dolore in dolore', 0, 0, 0, 0, '2022-11-07 22:44:20', '2022-11-07 22:44:20');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667833159073, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-11-07 23:01:20', '2022-11-07 23:01:20');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667833159073, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-11-07 23:01:28', '2022-11-07 23:01:28');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667833159073, '1558345740441477122', 'C:3#D:4', 'C:3', NULL, 0, 0, 0, '2022-11-07 23:04:17', '2022-11-07 23:04:17');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667833159073, '1558346432061235201', '41', 'dolore in dolore', 0, 0, 0, 0, '2022-11-07 23:04:17', '2022-11-07 23:04:17');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667833644366, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-11-07 23:08:24', '2022-11-07 23:08:24');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667833644366, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-11-07 23:08:24', '2022-11-07 23:08:24');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667833644366, '1558345740441477122', 'C:3#D:4', 'C:3', 10, 1, 0, 0, '2022-11-07 23:09:16', '2022-11-07 23:09:16');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667833644366, '1558346432061235201', '41', 'dolore in dolore', 0, 0, 0, 0, '2022-11-07 23:09:17', '2022-11-07 23:09:17');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667869037176, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-11-08 08:57:29', '2022-11-08 08:57:29');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667869037176, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-11-08 08:57:36', '2022-11-08 08:57:36');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667869037176, '1558345740441477122', 'C:3#D:4', 'C:3', 10, 1, 0, 0, '2022-11-08 08:58:12', '2022-11-08 08:58:12');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667869037176, '1558346432061235201', '41#123#11', '11', 0, 0, 0, 0, '2022-11-08 08:58:12', '2022-11-08 08:58:12');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667869179289, '1558345138294611969', 'C:3', 'sed enim', 0, 0, 0, 0, '2022-11-08 08:59:39', '2022-11-08 08:59:39');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667869179289, '1558344750178885633', 'B:2', 'dolore in dolore', 0, 0, 0, 0, '2022-11-08 08:59:39', '2022-11-08 08:59:39');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667869179289, '1558345740441477122', 'C:3#D:4', 'C:3', 10, 0, 0, 0, '2022-11-08 08:59:39', '2022-11-08 08:59:39');
INSERT INTO `ex_paper_student_answer` VALUES ('1559401362804965372', '1540618175123570690', 1667869179289, '1558346432061235201', '41#123#11', '11', 20, 1, 0, 0, '2022-11-08 08:59:59', '2022-11-08 08:59:59');

-- ----------------------------
-- Table structure for ex_question
-- ----------------------------
DROP TABLE IF EXISTS `ex_question`;
CREATE TABLE `ex_question`  (
  `question_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '题目表主键',
  `question_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '题目描述',
  `question_option` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项名称',
  `question_difficulty` tinyint UNSIGNED NOT NULL COMMENT '0：容易  1：中等  2：难',
  `question_type` tinyint UNSIGNED NOT NULL COMMENT '0：单选  1：多选  2：判断  3：填空',
  `question_answer_num` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '答案数量，默认1',
  `right_answer` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '正确答案',
  `question_answer_explain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '答案解析',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`question_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题目表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ex_question
-- ----------------------------
INSERT INTO `ex_question` VALUES ('1558344750178885633', '1+1等于几？', 'A:1<>B:2<>C:3<>D:4', 0, 0, 1, 'B:2', '因为1+1等于2', '1518868937486368710', 0, 0, '2022-08-13 14:48:36', '2022-08-13 14:48:36');
INSERT INTO `ex_question` VALUES ('1558345138294611969', '2+1等于几？', 'A:1<>B:2<>C:3<>D:4', 0, 0, 1, 'C:3', '因为2+1等于3', '1518868937486368710', 0, 0, '2022-08-13 14:50:08', '2022-08-13 14:50:08');
INSERT INTO `ex_question` VALUES ('1558345740441477122', '2小于下面哪几个数？', 'A:1<>B:2<>C:3<>D:4', 0, 1, 2, 'C:3#D:4', '因为2+1等于3', '1518868937486368710', 0, 0, '2022-08-13 14:52:32', '2022-08-13 14:52:32');
INSERT INTO `ex_question` VALUES ('1558346182504341506', '30大于20', '', 1, 2, 1, '正确', '因为数学', '1518868937486368710', 0, 0, '2022-08-13 14:54:17', '2022-08-13 14:54:17');
INSERT INTO `ex_question` VALUES ('1558346432061235201', '40和42中间的数字是？', '', 1, 3, 1, '41#123#11', 'nisi dolor', '1518868937486368710', 0, 0, '2022-08-13 14:55:17', '2022-08-13 15:24:15');

-- ----------------------------
-- Table structure for ex_question_paper
-- ----------------------------
DROP TABLE IF EXISTS `ex_question_paper`;
CREATE TABLE `ex_question_paper`  (
  `paper_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '试卷id',
  `question_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '题目id',
  `question_order` smallint UNSIGNED NULL DEFAULT NULL COMMENT '题目序号',
  `question_score` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '题目分数',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题目集——题目中间表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ex_question_paper
-- ----------------------------
INSERT INTO `ex_question_paper` VALUES ('1559401362804965378', '1558344750178885633', 1, 20, 0, 0, '2022-08-16 12:47:12', '2022-08-16 12:47:12');
INSERT INTO `ex_question_paper` VALUES ('1559401362804965372', '1558345138294611969', 2, 80, 0, 0, '2022-08-16 12:47:12', '2022-08-16 12:47:12');
INSERT INTO `ex_question_paper` VALUES ('1559402969504415746', '1558344750178885633', 1, 20, 0, 0, '2022-08-16 12:53:35', '2022-08-16 12:53:35');
INSERT INTO `ex_question_paper` VALUES ('1559402969504415746', '1558345138294611969', 2, 80, 0, 0, '2022-08-16 12:53:35', '2022-08-16 12:53:35');
INSERT INTO `ex_question_paper` VALUES ('1559403124014186497', '1558344750178885633', 1, 20, 0, 0, '2022-08-16 12:54:12', '2022-08-16 12:54:12');
INSERT INTO `ex_question_paper` VALUES ('1559403124014186497', '1558345138294611969', 2, 80, 0, 0, '2022-08-16 12:54:12', '2022-08-16 12:54:12');
INSERT INTO `ex_question_paper` VALUES ('1559403652140097538', '1558344750178885633', 1, 20, 0, 0, '2022-08-16 12:56:18', '2022-08-16 12:56:18');
INSERT INTO `ex_question_paper` VALUES ('1559403652140097538', '1558345138294611969', 2, 80, 0, 0, '2022-08-16 12:56:18', '2022-08-16 12:56:18');
INSERT INTO `ex_question_paper` VALUES ('1559401362804965372', '1558344750178885633', 1, 20, 0, 0, '2022-08-16 12:47:12', '2022-08-16 12:47:12');
INSERT INTO `ex_question_paper` VALUES ('1559401362804965378', '1558345138294611969', 2, 80, 0, 0, '2022-08-16 12:47:12', '2022-08-16 12:47:12');
INSERT INTO `ex_question_paper` VALUES ('1559401362804965372', '1558345740441477122', 3, 20, 0, 0, '2022-08-16 12:47:12', '2022-08-16 12:47:12');
INSERT INTO `ex_question_paper` VALUES ('1559401362804965372', '1558346432061235201', 4, 20, 0, 0, '2022-08-16 12:47:12', '2022-08-16 12:47:12');

-- ----------------------------
-- Table structure for ex_question_point
-- ----------------------------
DROP TABLE IF EXISTS `ex_question_point`;
CREATE TABLE `ex_question_point`  (
  `question_id` char(20) CHARACTER SET utf8 COLLATE utf8_czech_ci NOT NULL COMMENT '题目id',
  `point_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '知识点id',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题目知识点中间表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ex_question_point
-- ----------------------------
INSERT INTO `ex_question_point` VALUES ('1558344750178885633', '1548237376028262402', 0, 0, '2022-08-13 14:48:36', '2022-08-13 14:48:36');
INSERT INTO `ex_question_point` VALUES ('1558345138294611969', '1548237376028262402', 0, 0, '2022-08-13 14:50:08', '2022-08-13 14:50:08');
INSERT INTO `ex_question_point` VALUES ('1558345740441477122', '1548237376028262402', 0, 0, '2022-08-13 14:52:32', '2022-08-13 14:52:32');
INSERT INTO `ex_question_point` VALUES ('1558346182504341506', '1548237376028262402', 0, 0, '2022-08-13 14:54:17', '2022-08-13 14:54:17');
INSERT INTO `ex_question_point` VALUES ('1558346432061235201', '1548237376028262402', 0, 0, '2022-08-13 14:55:17', '2022-08-13 14:55:17');
INSERT INTO `ex_question_point` VALUES ('1558346432061235201', '1548237398266462210', 0, 0, '2022-08-13 15:24:15', '2022-08-13 15:24:15');

-- ----------------------------
-- Table structure for ex_user_question
-- ----------------------------
DROP TABLE IF EXISTS `ex_user_question`;
CREATE TABLE `ex_user_question`  (
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `question_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '题目id',
  `last_wrong_answer` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记录上一个错误答案',
  `wrong_time` smallint UNSIGNED NULL DEFAULT NULL COMMENT '题目做错次数',
  `make_time` smallint UNSIGNED NULL DEFAULT NULL COMMENT '题目做题次数',
  `is_collect` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否收藏，0是，默认1否',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户试题中间表\r\n用于体现用户和试题之间的关系' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ex_user_question
-- ----------------------------
INSERT INTO `ex_user_question` VALUES ('1540618175123570690', '1558344750178885633', 'dolore in dolore', 5, 21, 0, 23, 0, '2022-08-13 15:24:15', '2022-11-08 08:59:39');
INSERT INTO `ex_user_question` VALUES ('1540618175123570690', '1558345138294611969', 'sed enim', 5, 20, 0, 19, 0, '2022-10-23 23:05:23', '2022-11-08 08:59:39');
INSERT INTO `ex_user_question` VALUES ('1540618175123570690', '1558345740441477122', NULL, 1, 5, 1, 4, 0, '2022-11-07 22:44:20', '2022-11-08 08:59:39');
INSERT INTO `ex_user_question` VALUES ('1540618175123570690', '1558346432061235201', '11', 3, 5, 1, 4, 0, '2022-11-07 22:44:20', '2022-11-08 08:59:59');

-- ----------------------------
-- Table structure for st_user_point
-- ----------------------------
DROP TABLE IF EXISTS `st_user_point`;
CREATE TABLE `st_user_point`  (
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `point_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '知识点id',
  `point_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '知识点名字',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `level` tinyint UNSIGNED NOT NULL COMMENT '掌握水平，0-40未掌握，60部分掌握，80掌握',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁',
  `del_flag` tinyint UNSIGNED NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户知识点掌握统计分析表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of st_user_point
-- ----------------------------
INSERT INTO `st_user_point` VALUES ('1540618175123570690', '1548237376028262402', '命题', '1518868937486368710', 81, 1, 0, '2022-10-11 10:17:53', '2022-10-10 10:17:57');
INSERT INTO `st_user_point` VALUES ('1540618175123570690', '1548237398266462210', '真命题', '1518868937486368710', 71, 1, 0, '2022-10-18 10:18:24', '2022-10-11 10:18:27');
INSERT INTO `st_user_point` VALUES ('1540618175123570690', '1558262141767667713', '假命题', '1518868937486368710', 61, 1, 0, '2022-09-28 10:53:17', '2022-10-06 10:53:20');
INSERT INTO `st_user_point` VALUES ('1540618175123570690', '1558262788751642625', '命题规则', '1518868937486368710', 51, 1, 0, '2022-10-05 10:53:55', '2022-10-19 10:53:58');
INSERT INTO `st_user_point` VALUES ('1540618175123570690', '1558264003279474690', '一范式', '1518868937486368710', 41, 1, 0, '2022-10-04 10:54:27', '2022-10-06 10:54:30');
INSERT INTO `st_user_point` VALUES ('1540618175123570690', '1558264044052303874', '二范式', '1518868937486368710', 91, 1, 0, '2022-10-11 10:55:01', '2022-10-19 10:55:05');

-- ----------------------------
-- Table structure for us_article
-- ----------------------------
DROP TABLE IF EXISTS `us_article`;
CREATE TABLE `us_article`  (
  `article_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章内容',
  `summary` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章摘要',
  `is_top` tinyint NOT NULL DEFAULT 0 COMMENT '是否置顶（0否，1是）',
  `states` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0草稿, 1已发布）',
  `view_count` bigint NOT NULL DEFAULT 0 COMMENT '访问量 //TODO 准备删',
  `is_comment` tinyint NOT NULL DEFAULT 1 COMMENT '是否允许评论（0否，1是）',
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章所属用户id',
  `version` int NOT NULL COMMENT '乐观锁',
  `del_flag` int NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of us_article
-- ----------------------------
INSERT INTO `us_article` VALUES ('1589295012328550401', '采物原量第拉', 'tempor esse', 'nisi est consequat dolore pariatur', 0, 1, 0, 0, '1540618175123570690', 4, 0, '2022-11-07 00:33:53', '2022-11-07 11:18:47');
INSERT INTO `us_article` VALUES ('1589295268575358977', '采物原量第拉', 'tempor esse', 'nisi est consequat dolore pariatur', 0, 0, 0, 0, '1540618175123570690', 2, 0, '2022-11-07 00:34:54', '2022-11-07 00:39:16');
INSERT INTO `us_article` VALUES ('1590621268856815618', '水问电表步和', 'minim cillum ut Ut adipisicing', 'pariatur aliqua in proident', 0, 1, 0, 0, '1540618175123570690', 2, 0, '2022-11-10 16:23:57', '2022-11-10 16:31:17');

-- ----------------------------
-- Table structure for us_comment
-- ----------------------------
DROP TABLE IF EXISTS `us_comment`;
CREATE TABLE `us_comment`  (
  `comment_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '-1' COMMENT '评论id',
  `type` tinyint NOT NULL COMMENT '评论类型（0代表文章评论，1代表话题评论）',
  `blog_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论的文章或话题的id',
  `root_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '-1' COMMENT '根评论id（如果没有，则置为-1，只有两级评论）',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发表评论的用户id',
  `version` int NOT NULL COMMENT '乐观锁',
  `del_flag` int NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of us_comment
-- ----------------------------

-- ----------------------------
-- Table structure for us_relation
-- ----------------------------
DROP TABLE IF EXISTS `us_relation`;
CREATE TABLE `us_relation`  (
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `followed_user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '被关注用户id',
  `version` int NOT NULL COMMENT '乐观锁',
  `del_flag` int NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of us_relation
-- ----------------------------

-- ----------------------------
-- Table structure for us_topic
-- ----------------------------
DROP TABLE IF EXISTS `us_topic`;
CREATE TABLE `us_topic`  (
  `topic_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '话题id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '话题标题',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '话题内容',
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布话题的用户id',
  `course_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定的课程id',
  `version` int NULL DEFAULT NULL COMMENT '乐观锁',
  `del_flag` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`topic_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of us_topic
-- ----------------------------

-- ----------------------------
-- Table structure for us_user
-- ----------------------------
DROP TABLE IF EXISTS `us_user`;
CREATE TABLE `us_user`  (
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户姓名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `sex` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '用户性别（默认1男，0女）',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `mobile` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机号码',
  `head_portrait` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像路径地址',
  `personal_signature` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户个性签名',
  `school` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户学校',
  `version` int UNSIGNED NOT NULL COMMENT '乐观锁版本号',
  `del_flag` tinyint NOT NULL COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`, `sex`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户详情表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of us_user
-- ----------------------------
INSERT INTO `us_user` VALUES ('1523918834958868482', '张三', '$2a$10$DHpRiyzxetmWIKo6avJe5Oa/9PKgTd3WK1QFENkmX5/IFELEjEdma', 1, 'wiwi1111@163.com', '1213213', NULL, NULL, 'gpnu', 0, 0, '2022-05-03 16:58:21', '2022-05-03 16:58:21');
INSERT INTO `us_user` VALUES ('1540618175123570690', '李四', '$2a$10$lC8R7QRk3fz70MIq0wF3Huyo4LxjfM8VGN6LZ66.ZYWKnYchdnF2i', 1, 'wiwi1111@163.com', '15526448585', NULL, NULL, 'gpnu', 0, 0, '2022-05-10 14:51:58', '2022-05-10 14:51:58');
INSERT INTO `us_user` VALUES ('1569331764029952002', '测试', '$2a$10$RsRXcwFUi2AVGNo9Msad/eVPeVzxqwYXJc1uNdOoIjqpeSu7sc1zW', 1, 'w.wjebtk@qq.com', '18151185382', NULL, NULL, 'Ut minim', 0, 0, '2022-09-12 22:27:04', '2022-09-12 22:27:04');
INSERT INTO `us_user` VALUES ('1580730976818946050', '测试', '$2a$10$A.iLc.mKjwD.c2bl3Gy3tui26uCpBtMjMjuqWc9ukc9Ky2LJdMsHa', 0, 'd.ukwyyy@qq.com', '18117856316', NULL, NULL, 'cupidatat velit est cillum proident', 0, 0, '2022-10-14 09:23:28', '2022-10-14 09:23:28');

-- ----------------------------
-- Table structure for us_user_blog
-- ----------------------------
DROP TABLE IF EXISTS `us_user_blog`;
CREATE TABLE `us_user_blog`  (
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `blog_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标id',
  `type` tinyint NULL DEFAULT NULL COMMENT '博客类型 0 文章 1话题',
  `is_like` tinyint NULL DEFAULT 0 COMMENT '是否点赞 0否1是',
  `is_collect` tinyint NULL DEFAULT 0 COMMENT '是否收藏 0否1是',
  `version` int NULL DEFAULT NULL COMMENT '乐观锁',
  `del_flag` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`, `blog_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户文章中间表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of us_user_blog
-- ----------------------------
INSERT INTO `us_user_blog` VALUES ('1523918834958868482', '1589295012328550401', 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `us_user_blog` VALUES ('1540618175123570690', '1589295012328550401', 0, 1, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for us_user_comment
-- ----------------------------
DROP TABLE IF EXISTS `us_user_comment`;
CREATE TABLE `us_user_comment`  (
  `user_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `comment_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论id',
  `is_like` tinyint NOT NULL COMMENT '是否点赞  0否1是',
  PRIMARY KEY (`user_id`, `comment_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of us_user_comment
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
