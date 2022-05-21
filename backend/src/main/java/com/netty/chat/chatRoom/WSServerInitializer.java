package com.netty.chat.chatRoom;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //编解码
        pipeline.addLast(new HttpServerCodec());
        //大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        //聚合httpMessage 响应或者请求
        pipeline.addLast(new HttpObjectAggregator(1024*1024));

        //心跳机制
        pipeline.addLast(new IdleStateHandler(20, 40, 60));
        pipeline.addLast(new HeartBeatHandler());

        //webSocket的支持
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new MyServerHandler());
    }
}
