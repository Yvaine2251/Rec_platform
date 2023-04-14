//package com.platform.exam.constants;
//
//import lombok.Getter;
//
///**
// * @author yjj
// * @date 2022/8/16-10:43
// */
//@Getter
//public enum QuestionDifficulty {
//
//    EASY(0,"简单"),
//    MEDIUM(1,"中等"),
//    HARD(2,"困难");
//
//    private final Integer type;
//    private final String typeName;
//
//    QuestionDifficulty(Integer type, String typeName) {
//        this.type = type;
//        this.typeName = typeName;
//    }
//
//    public static String findQuestionDifficulty(Integer type){
//        if(type == 0){
//            return EASY.getTypeName();
//        }
//        if(type == 1){
//            return MEDIUM.getTypeName();
//        }
//        if(type == 2){
//            return HARD.getTypeName();
//        }
//        return "wrong";
//    }
//}
