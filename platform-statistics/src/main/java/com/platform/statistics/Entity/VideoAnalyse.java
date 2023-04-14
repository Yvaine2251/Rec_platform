package com.platform.statistics.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.platform.entity.BaseEntity;
import io.swagger.models.auth.In;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Yvaine
 * @date 2023/04/03/9:38
 * @description
 */
@Data
public class VideoAnalyse extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String userId;

    private String courseId;

    private LocalDateTime lastTime;

    //TODO 已学习、总学习时间的类型？
    private Instant studyTime;
    private Instant totalTime;

    //0未完成，1已完成
    private boolean isComplete;

}
