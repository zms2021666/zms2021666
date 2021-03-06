package com.exchange.quotation.websocket;

import com.exchange.quotation.properties.HuobiQuotationConnectionProperties;
import com.exchange.quotation.utils.GZipUtils;
import com.exchange.quotation.websocket.message.HuobiQuotationMessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class HuobiQuotationClientHandler extends SimpleChannelInboundHandler<Object> {

    @Autowired
    private WebSocketClientHandshaker handshaker;

    @Autowired
    private HuobiQuotationMessageHandler messageHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(channel, (FullHttpResponse) msg);
            ctx.newPromise().setSuccess();
            return;
        }else if (msg instanceof FullHttpResponse){
            FullHttpResponse response = (FullHttpResponse)msg;
            //this.listener.onFail(response.status().code(), response.content().toString(CharsetUtil.UTF_8));
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }else {
            WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof BinaryWebSocketFrame) {
                //??????????????????????????????????????????????????????????????????
                BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
                String receive = decodeByteBuf(binaryFrame.content());
                log.info("??????????????????????????????:{}", receive);
                messageHandler.onReceiveMessage(receive);
            } else if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) frame;
                String text = textWebSocketFrame.text();
                log.info("??????????????????????????????:{}", text);
            } else if (frame instanceof PongWebSocketFrame) {
                log.info("websocket client recived pong!");
            } else if (frame instanceof CloseWebSocketFrame) {
                log.info("WebSocket Client Received Closing.");
                channel.close();
            }
        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("websocket client disconnected.");
        // TODO ????????????????????????
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("websocket client??????????????????????????????{}",cause);
        if (!ctx.newPromise().isDone()) {
            ctx.newPromise().setFailure(cause);
        }
        ctx.channel().close();
        ctx.close();
    }

    /**
     * ????????????
     */
    private String decodeByteBuf(ByteBuf buf) throws Exception {
        byte[] temp = new byte[buf.readableBytes()];
        buf.readBytes(temp);
        // gzip ??????
        temp = GZipUtils.decompress(temp);
        return new String(temp, StandardCharsets.UTF_8);
    }
}
