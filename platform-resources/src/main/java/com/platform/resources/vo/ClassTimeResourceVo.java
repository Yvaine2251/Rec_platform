package com.platform.resources.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.github.classgraph.json.Id;
import lombok.Data;

/**
 * @author yjj
 * @date 2022/9/13-15:11
 */
@Data
public class ClassTimeResourceVo {
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

    /**
     * 已学习0未学习1已学
     */
    private Integer isStudy;
}
