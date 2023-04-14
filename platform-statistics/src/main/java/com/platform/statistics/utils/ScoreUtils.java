package com.platform.statistics.utils;

/**
 * @author Yvaine
 * @date 2023/3/28
 * @description 获取学生成绩层次
 */
public class ScoreUtils {
    public static String getScoreRange(int score) {
        String range = "";
        if (score >= 90 && score <= 100) {
            range = "优秀";
        } else if (score >= 80 && score <= 89) {
            range = "良好";
        } else if (score >= 70 && score <= 79) {
            range = "达标";
        } else if (score >= 60 && score <= 69) {
            range = "合格";
        } else {
            range = "不合格";
        }

        return range;
    }
}
