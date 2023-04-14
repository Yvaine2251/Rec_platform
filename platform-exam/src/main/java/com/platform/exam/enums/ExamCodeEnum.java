package com.platform.exam.enums;



public enum ExamCodeEnum {

    PAPER_RELEASE_ERROR(501, "发布失败"),
    PAPER_NOT_EXIST(502, "试卷不存在"),
    PAPER_NOT_START(503, "未开始"),
    PAPER_END(504, "已结束"),
    PAPER_CANNOT_REMAKE(505, "不能重考"),
    PAPER_NOT_RELEASE(506, "试卷未发布"),


    PAPER_CANNOT_ENTER(507, "不能进入考试"),
    PAPER_CANNOT_SUBMIT(508, "不能提交试卷"),
    PAPER_CANNOT_REVIEW(509, "不能查看试卷"),
    PAPER_CANNOT_REVIEW_SCORE(510, "不能查看成绩"),
    PAPER_CANNOT_REVIEW_ANSWER(511, "不能查看答案"),
    PAPER_TIMEOUT(512, "考试超时"),

    PAPER_CANNOT_SAVE(513, "不能保存试卷"),


    PAPER_NOT_DONE(513, "试卷未完成"),
    PAPER_HAS_REVIEW(514, "试卷已批阅"),

    QUE_CREATE_ERROR(521, "题目创建失败"),
    QUE_NOT_EXIST(522, "题目不存在"),
    QUE_ALREADY_COLLECT(523, "题目已收藏"),
    QUE_NOT_COLLECT(524, "题目未收藏"),
    QUE_BANK_IS_EMPTY(525, "题库为空"),
    QUE_NOT_WRONG(526, "题目不是错题"),
    ;


    ExamCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
