package com.platform.statistics.vo;

import lombok.Data;

/**
 * @author Yvaine
 * @date
 * @description
 */
@Data
public class PaperStudentWithNameVO {
    private String userId;
    private String userName;
    private int paperScore;
    private String scoreRange;
}
