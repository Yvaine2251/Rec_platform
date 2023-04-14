package com.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.entity.LoginUser;
import com.platform.entity.ResponseResult;
import com.platform.entity.User;
import com.platform.exception.PlatformException;
import com.platform.mapper.UserMapper;
import com.platform.service.EmailService;
import com.platform.service.UserService;
import com.platform.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.platform.enums.UserCodeEnum.*;

/**
 * @author ErrorRua
 * @date 2022/11/18
 * @description:
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sendMailer;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult sendVerifiedCode(String email) {
        // 0.防止恶意发送
        if (Objects.nonNull(redisCache.getCacheObject(email))) {
            throw new PlatformException(REQUEST_FREQUENTLY.getCode(), REQUEST_FREQUENTLY.getMessage());
        }

        // 1.生成验证码
        String code = generateCode(6);
        Context context = new Context();
        context.setVariable("code", code);

        // 2.发送邮件
        String text = templateEngine.process("emailVerifyCode", context);

        // 3.保存验证码
        redisCache.setCacheObject(email, code, 10, TimeUnit.MINUTES);

        sendEmail(email, "您的Rec_Platform验证码", text);
        return ResponseResult.okResult();
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            //邮件发件人
            helper.setFrom(sendMailer);
            //邮件收件人
            helper.setTo(to);
            //邮件主题
            helper.setSubject(subject);
            //邮件内容
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new PlatformException(EMAIL_SEND_ERROR.getCode(), EMAIL_SEND_ERROR.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        User user = userMapper.selectOne(queryWrapper);
        //如果没有找到用户就抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }

        //返回用户信息
        //TODO 查询权限信息封装
        return new LoginUser(user);
    }

    private String generateCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
    }
}
