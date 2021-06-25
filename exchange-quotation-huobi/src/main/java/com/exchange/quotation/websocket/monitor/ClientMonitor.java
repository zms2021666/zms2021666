package com.exchange.quotation.websocket.monitor;

public interface ClientMonitor {
    // 是否存活
    boolean isAlive();

    // 发送 ping
    void sendPing();

    // 发送 pong
    void sendPong();

    // 发送信息
    void send(String msg);
}
