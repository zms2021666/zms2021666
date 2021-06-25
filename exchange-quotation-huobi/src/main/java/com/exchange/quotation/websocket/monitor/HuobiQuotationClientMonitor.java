package com.exchange.quotation.websocket.monitor;

import cn.hutool.json.JSONObject;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class HuobiQuotationClientMonitor implements ClientMonitor {

    protected Channel channel;

    @Override
    public boolean isAlive() {
        return this.channel != null && this.channel.isActive();
    }

    @Override
    public void sendPing() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ping", System.currentTimeMillis());
        this.send(jsonObject.toString());
    }

    @Override
    public void sendPong() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pong", System.currentTimeMillis());
        this.send(jsonObject.toString());
    }


    @Override
    public void send(String msg) {
        if(!this.isAlive()){
            log.error("channel未激活，发送消息失败");
            return;
        }
        this.channel.writeAndFlush(new TextWebSocketFrame(msg));
        log.info("发送消息：{}",msg);
    }
}
