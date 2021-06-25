package com.exchange.quotation.websocket.message;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.exchange.quotation.constant.GlobalConstant;
import com.exchange.quotation.websocket.monitor.HuobiQuotationClientMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HuobiQuotationMessageHandler extends HuobiQuotationClientMonitor implements MessageHandler {

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    @Override
    public void onReceiveMessage(String msg) {
        // 更新收到消息最新时间
        if (msg.contains("ping")) {
            this.send(msg.replace("ping", "pong"));
            return;
        }
        if (msg.contains("pong")) {
            this.sendPing();
            return;
        }
        try {
            this.sendPong();
            GlobalConstant.wsMsgQueue.put(msg);
        } catch (InterruptedException e) {
            log.error("websocket message queue put 异常，异常信息：{}",e);
        }
    }

    @Override
    public void handleSubscribeMsg() {
        //　获取订阅的消息
        for(;;){
            try {
                String subMsg = GlobalConstant.subscribeQueue.take();
                JSONObject jsonObject = JSONUtil.parseObj(subMsg);
                String sub = (String)jsonObject.get("sub");
                GlobalConstant.subscribedSet.add(sub);
                this.send(subMsg);
            } catch (InterruptedException e) {
                log.error("获取队列消息失败,e:{}",e);
            }
        }
    }


    // 检测队列消息
    public ScheduledFuture startHandleSubMsgTask(){
        return scheduledExecutorService.schedule(()->{
            this.handleSubscribeMsg();
        },10, TimeUnit.SECONDS);
    }
}
