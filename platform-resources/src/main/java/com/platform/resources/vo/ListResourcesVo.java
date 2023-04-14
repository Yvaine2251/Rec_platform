package com.platform.resources.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yjj
 * @date 2022/9/18-9:46
 */
@Data
public class ListResourcesVo {
    /**
     * 资源id
     */
    private String resourceId;
    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建时间
     */
    private String resourceLink;
}
