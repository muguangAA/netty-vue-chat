package com.netty.chat.chatRoom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            if (event.state() == IdleState.READER_IDLE) {
                log.info(ctx.channel().id() + "读空闲");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info(ctx.channel().id() + "写空闲");
            } else if (event.state() == IdleState.ALL_IDLE) {
                ctx.channel().close();
            }
        }

    }
}
