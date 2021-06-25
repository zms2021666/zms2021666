package com.exchange.quotation.config;

import com.exchange.quotation.properties.HuobiQuotationConnectionProperties;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HuobiQuotationWebSocketConfig {


    @Bean
    public WebSocketClientHandshaker getWebSocketClientHandshaker(HuobiQuotationConnectionProperties connectionProperties){
        return WebSocketClientHandshakerFactory.newHandshaker(connectionProperties.getUri(), WebSocketVersion.V13, (String) null, true, new DefaultHttpHeaders());
    }


}
