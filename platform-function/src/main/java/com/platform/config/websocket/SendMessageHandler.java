package com.platform.config.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//暂时不用
@Slf4j
//@Component
public class SendMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的ChannelGroup
    public static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //存储 channel对应的username
    public static final Map<String, Channel> usernameChannelMap = new ConcurrentHashMap<>();
    public static final Map<Channel, String> channelUsernameMap = new ConcurrentHashMap<>();

    //用于接收用户名
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传输过来的消息
        String name = msg.text();
        //绑定user和channel
        usernameChannelMap.put(name, ctx.channel());
        channelUsernameMap.put(ctx.channel(), name);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 当客户端连接服务端之后，获取客户端的channel，并且放到ChannelGroup中去进行管理
        log.info("连接成功");
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 这步是多余的，当断开连接时候ChannelGroup会自动移除对应的channel
        String name = channelUsernameMap.get(ctx.channel());
        if(name != null){
            channelUsernameMap.remove(ctx.channel());
            usernameChannelMap.remove(name);
        }
        clients.remove(ctx.channel());
        System.out.println(ctx.channel().id().asLongText());
    }
}