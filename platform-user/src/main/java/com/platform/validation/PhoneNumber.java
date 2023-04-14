package com.platform.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;
import java.lang.reflect.Parameter;

/**
 * @author yjj
 * @date 2022/10/3-16:30
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "Invalid phone number";
    Class[] groups() default {};
    Class[] payload() default {};
}
