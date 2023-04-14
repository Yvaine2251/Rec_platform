package com.platform.springSecurity;

import com.platform.filter.JsonFilter;
import com.platform.filter.JwtAuthenticationTokenFilter;
import com.platform.filter.VerifyFilter;
import com.platform.handler.AccessDeniedHandlerImpl;
import com.platform.handler.AuthenticationEntryPointImpl;
import com.platform.security.EmailCodeAuthenticationProvider;
import com.platform.security.EmailPasswordAuthenticationFilter;
import com.platform.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class  SecurityConfig extends WebSecurityConfigurerAdapter {

    //此配置是为了修改SpringSecurity中的PasswordEncoder，替换为BCryptPasswordEncoder
    //创建BCryptPasswordEncoder注入容器
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private VerifyFilter verifyFilter;

    @Autowired
    private JsonFilter jsonFilter;
    @Autowired
    private EmailCodeAuthenticationProvider emailCodeAuthenticationProvider;
    @Bean
    public UserDetailsServiceImpl userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(emailCodeAuthenticationProvider);
    }

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //对于登录接口     anonymous()允许匿名访问，未登录可以访问，已登录不可访问
                //permitAll():无论有没有登录都可以访问
                .antMatchers("/user/login").anonymous()
                .antMatchers("/user/get-code").anonymous()
                .antMatchers("/user/registry").permitAll()
                .antMatchers("/resource/hello").permitAll()
                .antMatchers("/resources/upload").permitAll()
                .antMatchers("/resources/downloadResource").permitAll()
                .antMatchers("/chapter/**").permitAll()
                .antMatchers("/ctime/**").permitAll()
                .antMatchers("/**").permitAll()
                //除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        //关闭默认注销功能
        http.logout().disable();

//        http.authenticationProvider(daoAuthenticationProvider());

        http.authenticationProvider(emailCodeAuthenticationProvider)
                .addFilterBefore(new EmailPasswordAuthenticationFilter(authenticationManager()),UsernamePasswordAuthenticationFilter.class);

        //将自定义的过滤器放入到security的过滤器链中（第一个参数是自定义的过滤器对象，第二个参数是放置的位置）
        // TODO 不知道自定义过滤器怎么在第三方的过滤器之前执行
        http.addFilterBefore(jsonFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(verifyFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);


        //允许跨域
        http.cors();
    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
