package com.platform.config.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//暂时不用
@Slf4j
//@Component
public class WebSocketServer {

    /*
    * 启动netty服务器
    * */
    public void start(){
        log.info("netty启动");
        this.init();
    }

    private void init(){
        // 定义一对线程组  主线程组  用于接收客户端的连接请求，不做任何处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 定义一对线程组  从线程组  主线程组会把任务丢给从线程组，让从线程组去处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // netty服务器的创建，ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup) // 设置主从线程组
                    .channel(NioServerSocketChannel.class) // 设置NIO双向通道类型
                    .childHandler(new WebSocketServerInitializer()); // 子处理器，用于处理workerGroup

            // 启动server，绑定端口启动，并且同步等待方式启动
            ChannelFuture channelFuture = serverBootstrap.bind(9000).sync();

            // 监听关闭的channel， 设置为同步的方式
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭我们的主线程组和从线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}