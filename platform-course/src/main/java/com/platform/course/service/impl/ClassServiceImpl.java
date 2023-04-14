package com.platform.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.entity.ResponseResult;
import com.platform.course.dto.*;
import com.platform.course.entity.Class;
import com.platform.course.vo.*;
import com.platform.entity.User;
import com.platform.exception.PlatformException;
import com.platform.mapper.UserMapper;
import com.platform.course.service.ClassService;
import com.platform.course.entity.ClassUser;
import com.platform.course.entity.Course;
import com.platform.course.mapper.ClassMapper;
import com.platform.course.mapper.ClassUserMapper;
import com.platform.course.mapper.CourseMapper;
import com.platform.course.utils.ClassRandomCodeUtil;
import com.platform.points.entity.Point;
import com.platform.points.entity.UserPoint;
import com.platform.points.mapper.PointMapper;
import com.platform.points.mapper.UserPointMapper;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.platform.course.constants.CourseConstant.*;
import static com.platform.course.enums.CourseCodeEnum.*;

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Autowired
    private ClassRandomCodeUtil classRandomCodeUtil;

    @Autowired
    private ClassUserMapper classUserMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private ClassService classService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private UserPointMapper userPointMapper;

    /**
     * 处理班级创建，班级创建在课程里面创建的
     *
     * @param classCreateDto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult createClass(ClassCreateDTO classCreateDto) {
        String userId = SecurityUtils.getUserId();
        //判断是否有权限
        Course course = courseMapper.selectById(classCreateDto.getCourseId());
        String courseBuilderId = course.getCourseBuilderId();
        if (!userId.equals(courseBuilderId)) {
            throw new PlatformException(NOT_AUTHORITY.getCode(), NOT_AUTHORITY.getMessage());
        }

        //如果班级名在一门课程中重复，则不能创建
        LambdaQueryWrapper<Class> classWrapper = new LambdaQueryWrapper<>();
        classWrapper.eq(Class::getClassName, classCreateDto.getClassName());
        classWrapper.eq(Class::getCourseId, classCreateDto.getCourseId());
        Integer count = classMapper.selectCount(classWrapper);
        if (count != 0) {
            return ResponseResult.errorResult(CLASS_REPEAT_ERROR.getCode(), CLASS_REPEAT_ERROR.getMessage());
        }

        //新建班级
        Class newClass = BeanCopyUtils.copyBean(classCreateDto, Class.class);
        newClass.setClassInvitationCode(classRandomCodeUtil.randomCode());
        newClass.setStudentNumber(0);
        newClass.setTeacherId(userId);
        int result = classMapper.insert(newClass);
        if (result == 0) {
            throw new PlatformException(CLASS_CREATE_ERROR.getCode(), CLASS_CREATE_ERROR.getMessage());
        }
        return ResponseResult.okResult();
    }

    /**
     * 处理班级删除
     *
     * @param classId
     */
    @Override
    @Transactional
    public ResponseResult deleteClass(String classId) {
        String userId = SecurityUtils.getUserId();

        //判断是否有权限
        Class aClass = classMapper.selectById(classId);
        String teacherId = aClass.getTeacherId();
        if (!userId.equals(teacherId)) {
            throw new PlatformException(NOT_AUTHORITY.getCode(), NOT_AUTHORITY.getMessage());
        }

        int result = classMapper.deleteById(classId);
        if (result == 0) {
            throw new PlatformException(CLASS_DELETE_ERROR.getCode(), CLASS_DELETE_ERROR.getMessage());
        }

        //删除该班级下的学生
        LambdaQueryWrapper<ClassUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassUser::getClassId, classId);
        //注：简便的批量删除
        classUserMapper.delete(wrapper);
        return ResponseResult.okResult();
    }


    /**
     * 处理修改班级名称
     *
     * @param classUpdateDto
     */
    @Override
    public ResponseResult updateClassName(ClassUpdateDTO classUpdateDto) {
        String userId = SecurityUtils.getUserId();

        //判断是否有权限
        Class aClass = classMapper.selectById(classUpdateDto.getClassId());
        String teacherId = aClass.getTeacherId();
        if (!userId.equals(teacherId)) {
            throw new PlatformException(NOT_AUTHORITY.getCode(), NOT_AUTHORITY.getMessage());
        }
        aClass.setClassName(classUpdateDto.getClassName());
        int result = classMapper.updateById(aClass);
        if (result == 0) {
            throw new PlatformException(CLASS_UPDATE_ERROR.getCode(), CLASS_UPDATE_ERROR.getMessage());
        }
        return ResponseResult.okResult();
    }

    /**
     * 处理添加学生
     *
     * @param classAddStudent
     */
    @Override
    public ResponseResult addStudent(ClassAddStudentDTO classAddStudent) {
        String userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getMobile, classAddStudent.getMobile());
        User user = userMapper.selectOne(userWrapper);
        if (user == null) {
            throw new PlatformException(USER_NOT_EXIST.getCode(), USER_NOT_EXIST.getMessage());
        }
        ClassUser classUser = new ClassUser();
        classUser.setClassId(classAddStudent.getClassId());
        classUser.setUserId(user.getUserId());
        classUserMapper.insert(classUser);
        //TODO 这里貌似没有将班级人数进行加一
        return ResponseResult.okResult();
    }

    /**
     * 处理移除学生
     *
     * @param classUserDto
     */
    @Override
    public ResponseResult removeStudentFromClass(ClassUserDTO classUserDto) {
        LambdaQueryWrapper<ClassUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassUser::getClassId, classUserDto.getClassId());
        wrapper.eq(ClassUser::getUserId, classUserDto.getUserId());
        int delete = classUserMapper.delete(wrapper);
        if (delete == 0) {
            throw new PlatformException(CLASS_REMOVE_STUDENT_ERROR.getCode(), CLASS_REMOVE_STUDENT_ERROR.getMessage());
        }

        //班级人数减1
        LambdaUpdateWrapper<Class> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("student_number = student_number - 1")
                .eq(Class::getClassId, classUserDto.getClassId());
        classService.update(updateWrapper);

        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult joinPublicClass(String courseId) {
        //查询出公共班级
        LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Class::getCourseId, courseId).eq(Class::getIsPublic, CLASS_ALREADY_PUBLIC);
        Class publicClass = classMapper.selectOne(wrapper);
        //学生id 与 班级继续绑定(加入)
        String userId = SecurityUtils.getUserId();
        ClassUser classUser = new ClassUser(userId, publicClass.getClassId(), DEFAULT_ROLE);
        classUserMapper.insert(classUser);
        //公共班级人数加一
        publicClass.setStudentNumber(publicClass.getStudentNumber() + 1);
        classMapper.updateById(publicClass);

        return ResponseResult.okResult();
    }

    /**
     * 处理显示班级
     *
     * @param courseId
     */
    @Override
    public ResponseResult showClass(String courseId) {
        String userId = SecurityUtils.getUserId();
        //判断是否有权限
        Course course = courseMapper.selectById(courseId);
        String courseBuilderId = course.getCourseBuilderId();
        if (!userId.equals(courseBuilderId)) {
            throw new PlatformException(NOT_AUTHORITY.getCode(), NOT_AUTHORITY.getMessage());
        }

        LambdaQueryWrapper<Class> lambda = new LambdaQueryWrapper<>();
        lambda.eq(Class::getCourseId, courseId)
                .orderByDesc(Class::getUpdateTime);
        List<Class> selectClasses = classMapper.selectList(lambda);
        List<ClassVo> classVos = BeanCopyUtils.copyBeanList(selectClasses, ClassVo.class);
        return ResponseResult.okResult(classVos);
    }

    /**
     * 处理显示班级的学生列表（班级管理页面）
     *
     * @param classId
     */
    @Override
    public ResponseResult classUserByClassId(String classId) {
        //取出班级名
        Class selectClass = classMapper.selectById(classId);
        String className = selectClass.getClassName();

        //先找出班级学生
        LambdaQueryWrapper<ClassUser> lambda1 = new LambdaQueryWrapper<>();
        lambda1.eq(ClassUser::getClassId, classId).orderByDesc(ClassUser::getRole);
        List<ClassUser> selectClassUser = classUserMapper.selectList(lambda1);
        List<ClassUserVo> classUserVos = new ArrayList<>();

        //在把需要展示的数据进行赋值
        for (ClassUser classUser : selectClassUser) {
            ClassUserVo classUserVo = new ClassUserVo();
            User selectUser = userMapper.selectById(classUser.getUserId());//通过用户id查找用户
            classUserVo.setUserId(classUser.getUserId());
            classUserVo.setName(selectUser.getName());
            classUserVo.setMobile(selectUser.getMobile());
            classUserVo.setClassName(className);
            classUserVo.setJoinTime(classUser.getCreateTime());
            classUserVo.setRole(classUser.getRole());
            classUserVos.add(classUserVo);
        }
        return ResponseResult.okResult(classUserVos);
    }

    /**
     * 处理班级邀请码查看信息
     *
     * @param classInvitationCode
     */
    @Override
    public ResponseResult invitationCodeInfo(String classInvitationCode) {

        //查询班级
        LambdaQueryWrapper<Class> classWrapper = new LambdaQueryWrapper<>();
        classWrapper.eq(Class::getClassInvitationCode, classInvitationCode);
        Class selectClass = classMapper.selectOne(classWrapper);
        if (selectClass == null) {
            throw new PlatformException(CLASS_NOT_EXIST.getCode(), CLASS_NOT_EXIST.getMessage());
        }
        //查询课程
        LambdaQueryWrapper<Course> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.eq(Course::getCourseId, selectClass.getCourseId());
        Course selectCourse = courseMapper.selectOne(courseWrapper);
        //查询用户
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserId, selectCourse.getCourseBuilderId());
        User selectUser = userMapper.selectOne(userWrapper);
        //返回参数
        InvitationCodeInfoVo invitationCodeInfoVo = new InvitationCodeInfoVo();
        invitationCodeInfoVo.setClassId(selectClass.getClassId());
        invitationCodeInfoVo.setCourseName(selectCourse.getCourseName());
        invitationCodeInfoVo.setCourseCover(selectCourse.getCourseCover());
        invitationCodeInfoVo.setTeacherName(selectUser.getName());
        return ResponseResult.okResult(invitationCodeInfoVo);
    }

    /**
     * 处理加入课程 本质就是加入班级
     *
     * @param classId
     */
    @Override
    @Transactional
    public ResponseResult joinClass(String classId, String ability, String expect) {
        String userId = SecurityUtils.getUserId();
        //先判断是否已加入课程
        LambdaQueryWrapper<ClassUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassUser::getClassId, classId)
                .eq(ClassUser::getUserId, userId);
        if (classUserMapper.selectOne(wrapper) != null) {
            throw new PlatformException(CLASS_STUDENT_EXIST.getCode(), CLASS_STUDENT_EXIST.getMessage());
        }
        //将用户班级关联信息存入数据库
        ClassUser classUser = new ClassUser();
        classUser.setUserId(userId);
        classUser.setClassId(classId);
        classUser.setRole(0);
        int result = classUserMapper.insert(classUser);
        if (result == 0) {
            throw new PlatformException(CLASS_JOIN_ERROR.getCode(), CLASS_JOIN_ERROR.getMessage());
        }

        //班级人数加1
        Class aClass = classMapper.selectById(classId);
        aClass.setStudentNumber(aClass.getStudentNumber() + 1);
        classMapper.updateById(aClass);

        //初始化个人知识图谱
        //a较好，b合格	55
        //a较好，b良好；a一般，b合格	50
        //a较好，b优秀；a一般，b良好	45
        //a一般，b优秀	40

        LambdaQueryWrapper<Point> pointWrapper = new LambdaQueryWrapper<>();
        pointWrapper.eq(Point::getCourseId, aClass.getCourseId());
        List<Point> pointList = pointMapper.selectList(pointWrapper);

        if (ABILITY_PREFERABLY.equals(ability) && EXPECT_QUALIFIED.equals(expect)) {
            for (Point point : pointList) {
                UserPoint userPoint = new UserPoint();
                userPoint.setUserId(userId);
                userPoint.setPointId(point.getPointId());
                userPoint.setPointName(point.getPointName());
                userPoint.setCourseId(aClass.getCourseId());
                userPoint.setLevel(55);
                userPointMapper.insert(userPoint);
            }
        } else if (ABILITY_PREFERABLY.equals(ability) && EXPECT_PREFERABLY.equals(expect)
                || ABILITY_GENERAL.equals(ability) && EXPECT_QUALIFIED.equals(expect)){
            for (Point point : pointList) {
                UserPoint userPoint = new UserPoint();
                userPoint.setUserId(userId);
                userPoint.setPointId(point.getPointId());
                userPoint.setPointName(point.getPointName());
                userPoint.setCourseId(aClass.getCourseId());
                userPoint.setLevel(50);
                userPointMapper.insert(userPoint);
            }
        } else if (ABILITY_PREFERABLY.equals(ability) && EXPECT_EXCELLENT.equals(expect)
                || ABILITY_GENERAL.equals(ability) && EXPECT_PREFERABLY.equals(expect)){
            for (Point point : pointList) {
                UserPoint userPoint = new UserPoint();
                userPoint.setUserId(userId);
                userPoint.setPointId(point.getPointId());
                userPoint.setPointName(point.getPointName());
                userPoint.setCourseId(aClass.getCourseId());
                userPoint.setLevel(45);
                userPointMapper.insert(userPoint);
            }
        } else if (ABILITY_GENERAL.equals(ability) && EXPECT_EXCELLENT.equals(expect)){
            for (Point point : pointList) {
                UserPoint userPoint = new UserPoint();
                userPoint.setUserId(userId);
                userPoint.setPointId(point.getPointId());
                userPoint.setPointName(point.getPointName());
                userPoint.setCourseId(aClass.getCourseId());
                userPoint.setLevel(40);
                userPointMapper.insert(userPoint);
            }
        }
        return ResponseResult.okResult();
    }

    /**
     * 处理退出课程 本质就是退出班级
     *
     * @param classId
     */
    @Override
    @Transactional
    public ResponseResult quitCourse(String classId) {
        String userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<ClassUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassUser::getUserId, userId)
                .eq(ClassUser::getClassId, classId);
        classUserMapper.delete(wrapper);

        //班级人数减1
        LambdaUpdateWrapper<Class> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("student_number = student_number - 1")
                .eq(Class::getClassId, classId);
        classService.update(updateWrapper);
        return ResponseResult.okResult();
    }


}
