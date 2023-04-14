package com.platform.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务，在启动类上加上注解，然后把工作类声明为bean，方法上加@Scheduled注解
 * @author yjj
 * @date 2022/10/4-9:38
 */
@Component
public class TestJob {

    // "0/5 * * * * ?" 表示每隔5s执行一次
    // "0 0 1 * * ？" 表示每天凌晨一点执行
    @Scheduled(cron = "0/5 * * * * ?")
    public void testJob() {
//        System.out.println("i love");
    }
}
