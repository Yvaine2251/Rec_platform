package com.platform.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.course.entity.Chapter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {

//    List<String> getChapterIdsByPid(String Pid);
}
