package com.platform.exam.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("ex_user_question")
// 题目，用户中间表  n-n
public class QuestionUser extends BaseEntity {

    // 用户id
    private String userId;
    // 题目id
    private String questionId;
    // 上一个错误答案
    private String lastWrongAnswer;
    // 题目做错次数
    private Integer wrongTime;
    // 题目做题次数
    private Integer makeTime;
    // 是否收藏
    private Integer isCollect;
    // 是否正确
    private Integer lastSubmitIsCorrect;

}
