package com.platform.course.service;

import com.platform.entity.ResponseResult;
import com.platform.course.dto.ClassTimeAddDTO;
import com.platform.course.dto.ClassTimeUpdateDTO;


public interface ClassTimeService {

    ResponseResult addClassTime(ClassTimeAddDTO classTimeAddDto);

    ResponseResult deleteClassTime(String classTimeId);

    ResponseResult updateClassTime(ClassTimeUpdateDTO classTimeUpdateDto);

    ResponseResult addStudyRecord(String resourceId);
}
