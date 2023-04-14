package com.platform.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.course.entity.Class;
import com.platform.entity.ResponseResult;
import com.platform.course.dto.*;

public interface ClassService extends IService<Class> {
    ResponseResult createClass(ClassCreateDTO classCreateDto);//创建班级

    ResponseResult invitationCodeInfo(String classInvitationCode);//邀请码查看信息

    ResponseResult joinClass(String classId, String ability, String expect);//加入课程 （班级）

    ResponseResult updateClassName(ClassUpdateDTO classUpdateDto);//修改班级名

    ResponseResult addStudent(ClassAddStudentDTO classAddStudent);//添加学生

    ResponseResult deleteClass(String classId);//删除班级

    ResponseResult quitCourse(String classId);//退出课程 （班级）

    ResponseResult showClass(String courseId);//显示课程里的班级

    ResponseResult classUserByClassId(String classId);//显示班级学生列表 （班级管理页面）

    ResponseResult removeStudentFromClass(ClassUserDTO classUserDto);//移除学生

    ResponseResult joinPublicClass(String courseId);
}
