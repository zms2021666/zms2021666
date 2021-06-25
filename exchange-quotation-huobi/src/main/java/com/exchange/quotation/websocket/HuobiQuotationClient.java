package com.exchange.quotation.websocket;

import com.exchange.quotation.constant.GlobalConstant;
import com.exchange.quotation.constant.HuobiQuotationConstant;
import com.exchange.quotation.properties.HuobiQuotationConnectionProperties;
import com.exchange.quotation.websocket.message.HuobiQuotationMessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class HuobiQuotationClient {

    @Autowired
    private WebSocketClientHandler webSocketClientHandler;

    @Autowired
    private HuobiQuotationConnectionProperties connectionProperties;

    @Autowired
    private HuobiQuotationMessageHandler huobiQuotationMessageHandler;

    private EventLoopGroup loopGroup;
    private Bootstrap bootstrap;

    @PostConstruct
    public void initConnect(){
        this.loopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap().option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(webSocketClientHandler);
    }


    public void clientConnect() {
        try {
            Channel channel = bootstrap.connect(connectionProperties.getHost(),connectionProperties.getPort()).sync().channel();
            huobiQuotationMessageHandler.setChannel(channel);
            this.initSub();
        } catch (InterruptedException e) {
            log.error("连接火币失败，请检查地址是否正确");
        }
    }


    public void initSub() {
        log.info("初始订阅");
        subKline();
//        subMarketData();
//        subMarketDepth();
    }

    /**
     * 订阅k线数据
     */
    private void subKline(){
        HuobiQuotationConstant.TradingPair[] tradingPairs = HuobiQuotationConstant.TradingPair.values();
        for (HuobiQuotationConstant.TradingPair tradingPair : tradingPairs) {
            HuobiQuotationConstant.Period[] periods = HuobiQuotationConstant.Period.values();
            for (HuobiQuotationConstant.Period period : periods) {
                String subMsg = HuobiQuotationConstant.formatKlineSub(tradingPair, period);
                try {
                    GlobalConstant.symbolMap.put(tradingPair.getTrade(),tradingPair.getName());
                    GlobalConstant.subscribeQueue.put(subMsg);
                } catch (InterruptedException e) {
                    log.error("添加默认订阅交易对异常,e:{}",e);
                }
            }
        }
    }

    /**
     * 订阅市场概要数据
     * @return
     */
    private void subMarketData() {
        HuobiQuotationConstant.TradingPair[] tradingPairs = HuobiQuotationConstant.TradingPair.values();
        for (HuobiQuotationConstant.TradingPair tradingPair : tradingPairs) {
            String subMsg = HuobiQuotationConstant.formatMarketDetailSub(tradingPair);
            try {
                GlobalConstant.subscribeQueue.put(subMsg);
            } catch (InterruptedException e) {
                log.error("添加默认订阅交易对异常,e:{}",e);
            }
        }
    }

    /**
     * 订阅市场深度行情数据
     */
    private void subMarketDepth(){
        HuobiQuotationConstant.TradingPair[] tradingPairs = HuobiQuotationConstant.TradingPair.values();
        for (HuobiQuotationConstant.TradingPair tradingPair : tradingPairs) {
            String subMsg = HuobiQuotationConstant.formatMarketDepthSub(tradingPair,HuobiQuotationConstant.DepthType.STEP0);
            try {
                GlobalConstant.subscribeQueue.put(subMsg);
            } catch (InterruptedException e) {
                log.error("添加默认订阅交易对异常,e:{}",e);
            }
        }
    }
}
