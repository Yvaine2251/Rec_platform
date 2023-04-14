package com.platform.course.vo;


import com.platform.resources.vo.ClassTimeResourceVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("课时展示类")
@Data
public class ClassTimeVo {

    String classTimeId;

    String name;

    String chapterId;

    String paperId;

    String paperName;

    List<ClassTimeResourceVo> resource;
}
