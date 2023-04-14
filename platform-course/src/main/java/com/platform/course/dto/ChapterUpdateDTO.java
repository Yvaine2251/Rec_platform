package com.platform.course.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)//chapter_id 装换成chapterId
public class ChapterUpdateDTO {

    //章节号
    String chapterId;

    //修改名字
    String newName;
}
