package com.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("us_user")
public class User extends BaseEntity {


    @Id
    @TableId(type = IdType.ASSIGN_ID)
    private String userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 学校
     */
    private String school;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 头像路径地址
     */
    private String headPortrait;

}
