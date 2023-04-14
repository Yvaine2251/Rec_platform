package com.platform.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动前执行，如博客浏览量等
 * @author yjj
 * @date 2022/10/4-10:06
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        //将数据提前存到 redis 中
    }
}
