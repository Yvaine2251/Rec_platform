package com.platform.config.knife4j;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class knife4jConfiguration {

    @Bean("docket")
    public Docket docket(){
        //文档笔刷
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        //文档信息
        ApiInfo apiInfo = new ApiInfoBuilder().
                title("关于knife4j美化swagger2的demo").
                description("掌握简单的注解").
                termsOfServiceUrl("https://www.baidu.com").
                contact(new Contact(
                        "springfox-swagger",
                        "https://www.baidu.com",
                        "2649695477@qq.com")).
                version("1.0").
                build();
        docket.apiInfo(apiInfo);

        //设置docket显示的接口信息（范围，访问地址等）
        docket = docket.groupName("2.X")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.platform"))
                .apis(RequestHandlerSelectors.basePackage("com.platform.course"))
                .paths(PathSelectors.any())//指定访问路径可以通过的controllerr
                .build();
        return docket;
    }
}
