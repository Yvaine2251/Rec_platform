package com.platform.course.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRandomTeacherVO {

    //用户id
    private String userId;

    //用户名
    private String name;

    //用户头像
    private String headPortrait;
}
