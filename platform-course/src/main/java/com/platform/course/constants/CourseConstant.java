package com.platform.course.constants;

public class CourseConstant {

    //课程
    public final static Integer DEFAULT_CLASS_NUM = 0;
    public final static String DEFAULT_CLASS_NAME = "默认班级";
    public final static String PUBLIC_CLASS_NAME = "公共班级";
    public final static int DEFAULT_IS_PUBLIC = 0;
    public final static int ALREADY_PUBLIC = 1;

    //班级
    public final static int CLASS_ALREADY_PUBLIC = 1;
    public final static int DEFAULT_ROLE = 0;

    //章节
    public final static String ROOT_CHAPTER_PRE = "-1";
    public final static String CACHE_CHAPTER_KEY = "cache:chapter:";
    public final static Integer CACHE_CHAPTER_TTL = 30;

    //课时
    public final static Integer NULL_COURSE_TIME = 0;

    /**
     * 学习能力和学习预期
     */
    public final static String ABILITY_PREFERABLY = "较好";
    public final static String ABILITY_GENERAL= "一般";
    public final static String EXPECT_EXCELLENT = "优秀";
    public final static String EXPECT_PREFERABLY = "良好";
    public final static String EXPECT_QUALIFIED = "合格";

    //视频记录
    public static final Integer NOT_COMPLETE = 0;
    public static final Integer ALREADY_COMPLETE = 1;

}
