package com.platform.util;

import com.platform.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        String id = getLoginUser().getUser().getUserId();
        return "1".equals(id);
    }

    public static String getUserId() {
        return getLoginUser().getUser().getUserId();
    }
}