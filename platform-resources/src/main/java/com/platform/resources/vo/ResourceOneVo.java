package com.platform.resources.vo;

import lombok.Data;

/**
 * @author yjj
 * @date 2022/9/18-9:20
 */
@Data
public class ResourceOneVo {
    /**
     * 资源id
     */
    private String resourceId;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     *  资源类型
     */
    private Integer type;

    /**
     * 资源链接
     */
    private String resourceLink;
}
