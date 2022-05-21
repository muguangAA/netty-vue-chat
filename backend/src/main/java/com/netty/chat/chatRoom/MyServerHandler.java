package com.netty.chat.chatRoom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netty.chat.entity.Message;
import com.netty.chat.entity.UserChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyServerHandler extends
        SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup clients = new DefaultChannelGroup(
            GlobalEventExecutor.INSTANCE);

    /**
     * 监听客户端注册
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 新客户端连接,加入队列
        clients.add(ctx.channel());
    }

    /**
     * 监听客户端断开
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 整理队列
        clients.remove(ctx.channel());
    }

    /**
     * 读取客户端发过来的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
            throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(msg.text(), Message.class);

        UserChannel.put(message.getFrom(), ctx.channel());

        if ("heartbeat".equals(message.getEvent())) {
            log.info("心跳消息:" + "channel" + "[" + ctx.channel().id() + "]");
            return;
        }

        if ("all".equals(message.getTo())) {
            log.info("群聊消息:" + "channel" + "[" + "发送消息:" + msg.text());
            for (Channel channel : clients) {
                // 判断是否是当前用户的消息
                if (channel != ctx.channel()) {
                    channel.writeAndFlush(msgPot(msg.text()));
                }
            }
        } else {
            log.info("私聊消息:" + "channel" + "[" + ctx.channel().id() + "]" + "发送消息:" + msg.text());
            Channel targetChannel = UserChannel.get(message.getTo());
            targetChannel.writeAndFlush(msgPot(msg.text()));
        }
    }

    /**
     * 监听连接异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close(); // 关闭
    }

    /**
     * 封装消息
     *
     * @param msg
     * @return
     */
    public TextWebSocketFrame msgPot(String msg) {
        return new TextWebSocketFrame(msg);
    }

}
