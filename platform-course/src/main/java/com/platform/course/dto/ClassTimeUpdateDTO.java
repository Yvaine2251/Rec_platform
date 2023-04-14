package com.platform.course.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassTimeUpdateDTO extends ClassTimeAddDTO {
    //更新课时id
    @JsonProperty("id")
    @JSONField(name = "id")
    private String classTimeId;
}
