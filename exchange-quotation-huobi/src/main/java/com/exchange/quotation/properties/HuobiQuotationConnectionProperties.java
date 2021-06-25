package com.exchange.quotation.properties;

import com.exchange.quotation.constant.HuobiQuotationConstant;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.channels.UnsupportedAddressTypeException;


@Data
@Slf4j
@Component
public class HuobiQuotationConnectionProperties {

    private URI uri;
    private String host;
    private Integer port;
    private SslContext sslCtx;

    public HuobiQuotationConnectionProperties() {
        try {
            this.uri = new URI(HuobiQuotationConstant.WEBSOCKET_BASE_URL);
            this.host = this.uri.getHost() == null ? "127.0.0.1" : this.uri.getHost();
            String scheme = this.uri.getScheme() == null ? "http" : this.uri.getScheme();
            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                log.error("只能支持ws链接");
                throw new UnsupportedAddressTypeException();
            }
            if ( this.uri.getPort() == -1) {
                if ("http".equalsIgnoreCase(scheme) || "ws".equalsIgnoreCase(scheme)) {
                    this.port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    this.port = 443;
                } else {
                    this.port = -1;
                }
            } else {
                this.port = this.uri.getPort();
            }
            final boolean ssl = "wss".equalsIgnoreCase(scheme);
            if (ssl) {
                this.sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } else {
                sslCtx = null;
            }
        } catch (Exception e) {
            log.error("初始化[HuobiQuotationWebSocketConnectionURL]失败");
        }
    }
}
