package com.exchange.quotation.websocket.message;

public interface MessageHandler {

    // 接收消息
    void onReceiveMessage(String msg);

    void handleSubscribeMsg();

}
