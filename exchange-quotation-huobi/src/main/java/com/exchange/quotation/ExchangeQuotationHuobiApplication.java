package com.exchange.quotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeQuotationHuobiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeQuotationHuobiApplication.class, args);
    }

}
