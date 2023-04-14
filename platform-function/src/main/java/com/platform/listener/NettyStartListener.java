package com.platform.listener;

import com.platform.config.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/*
*  监听Spring容器启动完成，完成后启动 Netty 服务器
* */
//暂时不用
/*@Slf4j
@Component
public class NettyStartListener implements ApplicationRunner {

    @Resource
    private WebSocketServer webSocketServer;

    @Async //让线程异步执行，不影响主线程
    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.webSocketServer.start();
    }
}*/
