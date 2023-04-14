package com.platform.exam.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("ex_question_point")
// 题目，知识点中间表  m-n
public class QuestionPoint extends BaseEntity{

    private String questionId;

    private String pointId;
}
