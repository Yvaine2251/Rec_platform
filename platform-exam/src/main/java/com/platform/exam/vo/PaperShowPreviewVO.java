package com.platform.exam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author yjj
 * @date 2022/9/17-22:21
 * 给老师展示试卷
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaperShowPreviewVO {
    // 试卷id
    String paperId;

    // 试卷名字
    String paperName;

    // 题目集
    List<TchQuestionShowOneVO> questionOfPaperVos;
}
