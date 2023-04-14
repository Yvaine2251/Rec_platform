package com.platform.statistics.Entity;

import lombok.Data;

/**
 * @author Yvaine
 * @date
 * @description
 */
@Data
public class PaperStudentWithName {
    private String userId;
    private String userName;
    private int paperScore;
    private String scoreRange;
}
