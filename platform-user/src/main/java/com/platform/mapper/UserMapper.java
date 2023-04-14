package com.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.entity.User;
import com.platform.vo.LearnClassVo;

import java.util.List;

/*@Mapper
@Repository*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取课程和教师相关信息
     */
    List<LearnClassVo> getCourseAndTeacherInfo(String userId);

    User selectById(String userId);
}
