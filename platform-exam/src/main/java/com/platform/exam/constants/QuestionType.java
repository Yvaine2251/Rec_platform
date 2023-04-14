package com.platform.exam.constants;

import lombok.Getter;

/**
 * @author yjj
 * @date 2022/8/16-10:49
 */
@Getter
public enum QuestionType {

    SINGLE_CHOICE(0,"单选题"),
    MULTIPLE_CHOICE(1,"多选题"),
    JUDGEMENT(2,"判断题"),
    FILL_BLANK(3,"填空题"),
    SUBJECTIVE(4,"主观题"),
    ;

    private final Integer type;
    private final String typeName;

    QuestionType(Integer type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public static String findQuestionType(Integer type){
        if(type == 0){
            return SINGLE_CHOICE.getTypeName();
        }
        if(type == 1){
            return MULTIPLE_CHOICE.getTypeName();
        }
        if(type == 2){
            return JUDGEMENT.getTypeName();
        }
        if(type == 3){
            return FILL_BLANK.getTypeName();
        }
        if(type == 4){
            return SUBJECTIVE.getTypeName();
        }
        return "wrong";
    }
}
