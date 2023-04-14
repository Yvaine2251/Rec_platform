package com.platform.statistics.vo;

import com.platform.course.entity.ClassUser;
import com.platform.entity.User;
import com.platform.points.entity.UserPoint;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @author Yvaine
 * @Date 2023/3/28
 * @description  学生知识点掌握度VO
 */
@Data
public class AnalysePointVO {

    private HashMap<ClassUser, List<UserPointVO>> userPointMap;
}
