package com.exchange.quotation.constant;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class GlobalConstant {

    // websocket连接重试次数
    public static final Integer number = 3;

    // 订阅交易对处理队列
    public static LinkedBlockingQueue<String> subscribeQueue = new LinkedBlockingQueue<String>();

    // 已经订阅的交易对
    public static Set<String> subscribedSet = new HashSet<>();

    // websocket 消息处理队列
    public static LinkedBlockingQueue<String> wsMsgQueue = new LinkedBlockingQueue<String>();

    // 交易对对应的symbol btcusdt -> BTC/USDT
    public static Map<String, String> symbolMap = new HashMap<>();

}
