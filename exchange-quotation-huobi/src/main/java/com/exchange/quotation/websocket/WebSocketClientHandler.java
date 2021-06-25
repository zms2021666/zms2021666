package com.exchange.quotation.websocket;

import com.exchange.quotation.properties.HuobiQuotationConnectionProperties;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketClientHandler extends ChannelInitializer<SocketChannel> {

    @Autowired
    private SimpleChannelInboundHandler channelHandler;

    @Autowired
    private HuobiQuotationConnectionProperties connectionProperties;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        if (connectionProperties.getSslCtx() != null) {
            pipeline.addLast(connectionProperties.getSslCtx().newHandler(socketChannel.alloc(), connectionProperties.getHost(), connectionProperties.getPort()));
        }
        //pipeline可以同时放入多个handler,最后一个为自定义hanler
        pipeline.addLast(new HttpClientCodec(), new HttpObjectAggregator(1024*10), channelHandler);
    }
}
