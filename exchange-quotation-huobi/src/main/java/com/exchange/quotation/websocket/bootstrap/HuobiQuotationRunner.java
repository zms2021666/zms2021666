package com.exchange.quotation.websocket.bootstrap;

import com.exchange.quotation.common.SpringContextHolder;
import com.exchange.quotation.websocket.HuobiQuotationClient;
import com.exchange.quotation.websocket.message.HuobiQuotationMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class HuobiQuotationRunner implements ApplicationRunner {

    @Autowired
    HuobiQuotationClient huobiQuotationClient;

    @Autowired
    HuobiQuotationMessageHandler messageHandler;

    @Override
    public void run(ApplicationArguments args)  {

        huobiQuotationClient.clientConnect();
        messageHandler.startHandleSubMsgTask();


    }
}
