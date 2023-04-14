package com.platform.dto;


import com.platform.validation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Size;

// TODO 字段校验有得完善

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistryDto {

    @Size(max = 20, min = 1, message = "姓名需要在1-20个数之间")
    @NotEmpty(message = "姓名不能为空")
    private String name;

    @Length(max = 15, min = 6, message = "密码为6-15位")
    @NotEmpty(message = "密码不能为空")
    private String password;

    private Integer sex;

    private String school;

//    @PhoneNumber(message = "手机号格式不正确")
//    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    private String email;

    private String emailVerifyCode;
}
