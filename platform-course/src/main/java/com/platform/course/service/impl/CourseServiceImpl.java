package com.platform.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.course.constants.CourseConstant;
import com.platform.course.entity.Chapter;
import com.platform.course.mapper.ChapterMapper;
import com.platform.course.service.ChapterService;
import com.platform.course.vo.*;
import com.platform.entity.ResponseResult;
import com.platform.course.dto.CourseUpdateDTO;
import com.platform.course.entity.Class;
import com.platform.course.entity.ClassUser;
import com.platform.course.mapper.ClassMapper;
import com.platform.course.mapper.ClassUserMapper;
import com.platform.course.service.ClassService;
import com.platform.course.service.CourseService;
import com.platform.course.dto.CourseCreateDTO;
import com.platform.course.entity.Course;

import com.platform.course.mapper.CourseMapper;
import com.platform.course.utils.ClassRandomCodeUtil;
import com.platform.entity.User;
import com.platform.exception.PlatformException;
import com.platform.mapper.UserMapper;
import com.platform.points.entity.Point;
import com.platform.points.mapper.PointMapper;
import com.platform.resources.entity.Resources;
import com.platform.resources.mapper.ResourcesMapper;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import com.platform.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.platform.course.enums.CourseCodeEnum.*;
import static com.platform.course.constants.CourseConstant.*;

/**
 * 新建课程
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private ClassUserMapper classUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private ClassService classService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ClassRandomCodeUtil classRandomCodeUtil;

    /**
     * 课程创建
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createCourse(CourseCreateDTO courseCreateDto) {
        //创建课程
        Course course = BeanCopyUtils.copyBean(courseCreateDto, Course.class);
        String userId = SecurityUtils.getUserId();
        course.setCourseBuilderId(userId);

        int insertCourse = courseMapper.insert(course);

        //创建课程里的默认班级
//        Class defaultClass = new Class();
//        defaultClass.setClassName(DEFAULT_CLASS_NAME);
//        defaultClass.setStudentNumber(DEFAULT_CLASS_NUM);
//        defaultClass.setTeacherId(userId);
//        defaultClass.setClassInvitationCode(classRandomCodeUtil.randomCode());
//        defaultClass.setCourseId(course.getCourseId());
//        defaultClass.setIsPublic(DEFAULT_IS_PUBLIC);
//        int insertClass = classMapper.insert(defaultClass);
        int insertClass = initClass(course, userId, DEFAULT_CLASS_NAME, DEFAULT_IS_PUBLIC);

        //如果是公共课程则创建一个公共班级
        if(courseCreateDto.getIsPublic() == ALREADY_PUBLIC){
            int insert = initClass(course, userId, PUBLIC_CLASS_NAME, ALREADY_PUBLIC);
            if(insert != 1){
                throw new PlatformException(COURSE_CREATE_ERROR.getCode(), COURSE_CREATE_ERROR.getMessage());
            }
        }

        if (insertCourse + insertClass != 2) {
            throw new PlatformException(COURSE_CREATE_ERROR.getCode(), COURSE_CREATE_ERROR.getMessage());
        }
        return ResponseResult.okResult();
    }

    //初始化班级
    private int initClass(Course course,String userId, String name, int isPublic) {
        Class defaultClass = new Class();
        defaultClass.setClassName(name);
        defaultClass.setIsPublic(isPublic);

        defaultClass.setStudentNumber(DEFAULT_CLASS_NUM);
        defaultClass.setTeacherId(userId);
        defaultClass.setClassInvitationCode(classRandomCodeUtil.randomCode());
        defaultClass.setCourseId(course.getCourseId());
        return classMapper.insert(defaultClass);
    }

    /**
     * 处理显示创建课程
     */
    @Override
    public ResponseResult showCreateCourse() {
        String userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getCourseBuilderId, userId)
                .orderByDesc(Course::getUpdateTime);
        List<Course> courses = courseMapper.selectList(wrapper);
        List<CourseVo> courseVos = BeanCopyUtils.copyBeanList(courses, CourseVo.class);
        return ResponseResult.okResult(courseVos);
    }

    /**
     * 处理获取我学的课 本质就是显示加入的班级
     */
    @Override
    public ResponseResult showJoinCourse() {
        String userId = SecurityUtils.getUserId();
        //先找出班级id
        LambdaQueryWrapper<ClassUser> lambda1 = new LambdaQueryWrapper<>();
        lambda1.eq(ClassUser::getUserId, userId);
        List<ClassUser> classUsers = classUserMapper.selectList(lambda1);
        List<CourseJoinShowVo> courseJoinVos = new ArrayList<>();
        //根据班级id找出班级以及课程信息
        for (ClassUser classUser : classUsers) {
            Class oneClass = classMapper.selectById(classUser.getClassId());
            Course course = courseMapper.selectById(oneClass.getCourseId());
            CourseJoinShowVo courseJoinVo = BeanCopyUtils.copyBean(course, CourseJoinShowVo.class);
            User user = userMapper.selectById(course.getCourseBuilderId());
            courseJoinVo.setTeacherName(user.getName());
            courseJoinVos.add(courseJoinVo);
        }
        return ResponseResult.okResult(courseJoinVos);
    }

    /**
     * 处理修改课程名称
     *
     * @param courseUpdateDto
     */
    @Override
    public ResponseResult updateCourse(CourseUpdateDTO courseUpdateDto) {
        String userId = SecurityUtils.getUserId();
        Course course = courseMapper.selectById(courseUpdateDto.getCourseId());
        
        //判断是否有权限修改课程，只有创建课程的人的才能修改该课程
        String courseBuilderId = course.getCourseBuilderId();
        if (!userId.equals(courseBuilderId)) {
            throw new PlatformException(NOT_AUTHORITY.getCode(), NOT_AUTHORITY.getMessage());
        }
        //判断是否改成了公开课程，如果改成公开课程则创建公共班级
        boolean createFlag = (course.getIsPublic() == 0) && (courseUpdateDto.getIsPublic() == 1);
        //判断是否关闭了公开课程,如果改成私有则删除公共班级
        boolean deleteFlag = (course.getIsPublic() == 1) && (courseUpdateDto.getIsPublic() == 0);
        course = BeanCopyUtils.copyBean(courseUpdateDto, Course.class);
        //创建公共课程
        if(createFlag){
            int insert = initClass(course, userId, PUBLIC_CLASS_NAME, ALREADY_PUBLIC);
            if(insert == 0){
                throw new PlatformException(COURSE_UPDATE_ERROR.getCode(), COURSE_UPDATE_ERROR.getMessage());
            }
        }
        //删除公共课程
        if(deleteFlag){
            //找到公共班级
            LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Class::getCourseId, courseUpdateDto.getCourseId()).eq(Class::getIsPublic, CLASS_ALREADY_PUBLIC);
            Class publicClass = classMapper.selectOne(wrapper);
            //删除班级与用户的绑定
            LambdaQueryWrapper<ClassUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ClassUser::getClassId, publicClass.getClassId());
            classUserMapper.delete(queryWrapper);
            //删除公共班级
            classMapper.deleteById(publicClass.getClassId());
        }
        int result = courseMapper.updateById(course);
        if (result == 0) {
            throw new PlatformException(COURSE_UPDATE_ERROR.getCode(), COURSE_UPDATE_ERROR.getMessage());
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteCourse(String courseId) {
        String userId = SecurityUtils.getUserId();

        Course course = courseMapper.selectById(courseId);
        //判断是否有权限修改课程，只有创建课程的人的才能修改该课程
        String courseBuilderId = course.getCourseBuilderId();
        if (!userId.equals(courseBuilderId)) {
            throw new PlatformException(NOT_AUTHORITY.getCode(), NOT_AUTHORITY.getMessage());
        }
        //删除课程
        int delete = courseMapper.deleteById(courseId);
        if (delete != 1) {
            throw new PlatformException(COURSE_DELETE_ERROR.getCode(), COURSE_DELETE_ERROR.getMessage());
        }

        //删除课程下的班级
        LambdaQueryWrapper<Class> classQueryWrapper = new LambdaQueryWrapper<>();
        classQueryWrapper.eq(Class::getCourseId, courseId);
        List<Class> classes = classMapper.selectList(classQueryWrapper);
        for (Class oneClass : classes) {
            //调用写好的删除班级，方法里包含了删除学生
            classService.deleteClass(oneClass.getClassId());
        }

        //删除课程下的章节，调用写好的删除章节接口，会把里面的课时也删除
        LambdaQueryWrapper<Chapter> chapterWrapper = new LambdaQueryWrapper<>();
        chapterWrapper.eq(Chapter::getCourseId, courseId)
                .select(Chapter::getId);
        List<Chapter> chapters = chapterMapper.selectList(chapterWrapper);
        for (Chapter chapter : chapters) {
            chapterService.deleteChapter(chapter.getId());
        }

        //删除课程资源
        LambdaQueryWrapper<Resources> resourcesWrapper = new LambdaQueryWrapper<>();
        resourcesWrapper.eq(Resources::getCourseId, courseId);
        resourcesMapper.delete(resourcesWrapper);

        //删除课程知识点及知识点中间表
        LambdaQueryWrapper<Point> pointWrapper = new LambdaQueryWrapper<>();
        pointWrapper.eq(Point::getCourseId, courseId);
        pointMapper.delete(pointWrapper);

        //TODO 删除课程试卷

        //TODO 删除课程题目
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectOneCourse(String courseId) {
        Course course = courseMapper.selectById(courseId);
        CourseVo courseVo = BeanCopyUtils.copyBean(course, CourseVo.class);
        return ResponseResult.okResult(courseVo);
    }

    //返回随机的课程
    @Override
    public ResponseResult getRandomCourse() {

        //查询所有公共课程
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getIsPublic, ALREADY_PUBLIC);
        List<Course> courses = courseMapper.selectList(queryWrapper);
        List<CourseRandomVO> courseRandomVOS = new ArrayList<>();
        //Math.random 0.0 - 1.0;
        for(int i = 0; i < 5; i ++){
            int index = (int)(Math.random() * courses.size());
            //将随机到的课取出来
            Course course = courses.get(index);
            courses.remove(index);
            CourseRandomVO courseRandomVO = BeanCopyUtils.copyBean(course, CourseRandomVO.class);
            //课程创建者的学校
            String builderId = course.getCourseBuilderId();
            User user = getUser(builderId);
            courseRandomVO.setSchool(user.getSchool());
            //课程总学生人数
            //找出该课程对应所有的班级
            int studentSum = getStudentSum(course);
            courseRandomVO.setCourseStuCnt(studentSum);
            courseRandomVOS.add(courseRandomVO);
        }

        return ResponseResult.okResult(courseRandomVOS);
    }

    //通过id找user
    private User getUser(String userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, userId);
        return userMapper.selectOne(queryWrapper);
    }

    //课程下班级的总学生数
    private int getStudentSum(Course course) {
        LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Class::getCourseId, course.getCourseId());
        List<Class> classes = classMapper.selectList(wrapper);
        int studentSum = 0;
        for(Class cla : classes){
            studentSum += cla.getStudentNumber();
        }
        return studentSum;
    }

    @Override
    public ResponseResult getRandomCourseDetail(String courseId) {
        //获取课程
        Course course = courseMapper.selectById(courseId);
        CourseRandomDetailVO courseRandomDetailVO = BeanCopyUtils.copyBean(course, CourseRandomDetailVO.class);
        //获取学校
        User user = getUser(course.getCourseBuilderId());
        courseRandomDetailVO.setSchool(user.getSchool());
        //获取总学生数
        int studentSum = getStudentSum(course);
        courseRandomDetailVO.setCourseStuCnt(studentSum);
        //封装教师(暂定创建人)
        CourseRandomTeacherVO courseRandomTeacherVO =
                new CourseRandomTeacherVO(user.getUserId(), user.getName(), user.getHeadPortrait());
        courseRandomDetailVO.setTeacher(courseRandomTeacherVO);

        return ResponseResult.okResult(courseRandomDetailVO);
    }
}
