package com.platform.course.service;

import com.platform.entity.ResponseResult;
import com.platform.course.dto.CourseCreateDTO;
import com.platform.course.dto.CourseUpdateDTO;

public interface CourseService {
    ResponseResult createCourse(CourseCreateDTO courseCreateDto);//创建课程

    ResponseResult showCreateCourse();//显示创建课程

    ResponseResult showJoinCourse();//显示加入的课程 （班级)

    ResponseResult updateCourse(CourseUpdateDTO courseUpdateDto);

    ResponseResult deleteCourse(String courseId);

    ResponseResult selectOneCourse(String courseId);

    ResponseResult getRandomCourse();

    ResponseResult getRandomCourseDetail(String courseId);
}
