package com.platform.vo;

import lombok.Data;

/**
 * @author ErrorRua
 * @date 2022/11/17
 * @description:
 */
@Data
public class VerifyCodeVO {

    private String verifyKey;
    private String codeImg;
}
