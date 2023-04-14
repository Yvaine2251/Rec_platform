package com.platform.security;

import com.platform.exception.PlatformException;
import com.platform.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author ErrorRua
 * @date 2022/11/24
 * @description:
 */
@Slf4j
@Component
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        log.info("EmailCodeAuthentication authentication request: {}", authentication);
        EmailCodeAuthenticationToken token = (EmailCodeAuthenticationToken) authentication;

        UserDetails user = emailService.loadUserByEmail((String) token.getPrincipal());

        System.out.println(token.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        System.out.println(user.getAuthorities());
        EmailCodeAuthenticationToken result =
                new EmailCodeAuthenticationToken(user, user.getAuthorities());
                /*
                Details 中包含了 ip地址、 sessionId 等等属性 也可以存储一些自己想要放进去的内容
                */
        result.setDetails(token.getDetails());

        additionalAuthenticationChecks(user, token);

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  EmailCodeAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new PlatformException(HttpStatus.UNAUTHORIZED.value(),"用户名或密码错误");

        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new PlatformException(HttpStatus.UNAUTHORIZED.value(),"用户名或密码错误");
        }
    }
}
