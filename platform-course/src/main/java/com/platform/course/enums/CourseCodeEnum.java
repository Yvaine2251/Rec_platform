package com.platform.course.enums;

import com.platform.constants.BaseCode;
import lombok.Getter;

/**
 * @author yjj
 * @date 2022/7/13-20:44
 */
@Getter
public enum CourseCodeEnum implements BaseCode {

    NOT_AUTHORITY(599, "没有权限"),

    //课程
    COURSE_CREATE_ERROR(501, "课程创建失败"),
    COURSE_UPDATE_ERROR(502, "课程修改失败"),
    COURSE_DELETE_ERROR(503, "课程删除失败"),

    //班级
    CLASS_REPEAT_ERROR(521, "班级名重复"),
    CLASS_CREATE_ERROR(522, "班级创建失败"),
    CLASS_DELETE_ERROR(523, "班级删除失败"),
    CLASS_UPDATE_ERROR(524, "班级名修改失败"),
    CLASS_NOT_EXIST(525, "班级不存在"),
    CLASS_STUDENT_EXIST(526, "加入失败，你已在该班级里"),
    CLASS_JOIN_ERROR(527, "班级加入失败"),
    USER_NOT_EXIST(528, "该用户不存在"),
    CLASS_REMOVE_STUDENT_ERROR(529, "移除学生失败"),

    //章节
    CHAPTER_INSERT_ERROR(541, "章节插入失败"),
    CHAPTER_UPDATE_ERROR(542, "章节更新失败"),

    //课时
    CLASS_TIME_ADD_ERROR(561, "课时新增失败"),
    CLASS_TIME_DELETE_ERROR(562, "课时删除失败"),
    CLASS_TIME_NOT_EXIST(563, "课时不存在"),

    CHAPTER_NOT_EXIST(581, "章节不存在"),
    ;

    private Integer code;
    private String message;

    CourseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
