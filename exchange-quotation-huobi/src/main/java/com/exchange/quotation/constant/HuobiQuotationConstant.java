package com.exchange.quotation.constant;

import cn.hutool.json.JSONObject;

import java.util.Arrays;
import java.util.UUID;

public interface HuobiQuotationConstant {
    /**
     * http请求url
     */
    public static final String HTTP_BASE_URL = "https://api.huobi.de.com/";

    /**
     * websocket 连接url
     */
    public static final String WEBSOCKET_BASE_URL = "wss://api.huobi.de.com/ws";

    /**
     * K线订阅
     */
    public static final String KLINE_SUB = "market.%s.kline.%s";

    /**
     * 交易深度
     */
    public static final String MARKET_DEPTH_SUB = "market.%s.depth.%s";

    /**
     * 成交明细
     */
    public static final String TRADE_DETAIL_SUB = "market.%s.trade.detail";

    /**
     * 市场概要
     */
    public static final String MARKET_DETAIL_SUB = "market.%s.detail";

    /**
     * 买一卖一逐笔行情
     */
    public static final String STROKE_QUOTES_SUB = "market.%s.bbo";


    /**
     * 时间
     */
    enum Period {
//        _1min("1min");
        _5min("5min");
//        _15min("15min"),
//        _30min("30min"),
//        _60min("60min"),
//        _4hour("4hour"),
//        _1day("1day"),
//        _1mon("1mon"),
//        _1week("1week"),
//        _1year("1year");

        private String period;

        Period(String period) {
            this.period = period;
        }


        public String getPeriod() {
            return this.period;
        }

        public static Period getPeriod(String period) {
            return Arrays.stream(Period.values()).filter(item -> item.getPeriod().equals(period)).findAny().orElse(null);
        }
    }

    /**
     * 交易对
     */
    enum TradingPair {
        BCTUSDT("btcusdt","BTC/USDT");
//        ,
//        ETHUSDT("ethusdt","ETH/USDT"),
//        EOSUSDT("eosusdt","EOS/USDT");
        private String trade;
        private String name;

        TradingPair(String trade,String name) {
            this.trade = trade;
            this.name = name;
        }

        public String getTrade() {
            return this.trade;
        }

        public String getName(){
            return this.name;
        }

    }


    /**
     * 深度类型
     */
    enum DepthType {
        STEP0("step0"),
        STEP1("step1"),
        STEP2("step2"),
        STEP3("step3"),
        STEP4("step4"),
        STEP5("step5");

        private String step;

        DepthType(String step) {
            this.step = step;
        }

        public String getStep() {
            return this.step;
        }
    }


    /**
     * 转换成k线订阅
     *
     * @param tradingPair 交易对
     * @param period      周期
     * @return
     */
    static String formatKlineSub(TradingPair tradingPair, Period period) {
        String subId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sub", String.format(HuobiQuotationConstant.KLINE_SUB, tradingPair.getTrade(), period.getPeriod()));
        jsonObject.put("id", subId);
        return jsonObject.toString();
    }


    /**
     * 转换成k线历史订阅
     *
     * @param tradingPair 交易对
     * @param period      周期
     * @return
     */
    static String formatKlineHistorySub(TradingPair tradingPair, Period period ,Long start,Long end) {
        String subId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("req", String.format(HuobiQuotationConstant.KLINE_SUB, tradingPair.getTrade(), period.getPeriod()));
        jsonObject.put("id", subId);
        jsonObject.put("from",start);
        jsonObject.put("to",end);
        return jsonObject.toString();
    }


    /**
     * 转换市场行情订阅
     *
     * @param tradingPair 交易对
     * @param depthType   深度
     *                    step0	 不合并深度
     *                    step1	 Aggregation level = precision*10
     *                    step2	 Aggregation level = precision*100
     *                    step3	 Aggregation level = precision*1000
     *                    step4	 Aggregation level = precision*10000
     *                    step5	 Aggregation level = precision*100000
     * @return
     */
    static String formatMarketDepthSub(TradingPair tradingPair, DepthType depthType) {
        String subId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sub", String.format(HuobiQuotationConstant.MARKET_DEPTH_SUB, tradingPair.getTrade(), depthType.getStep()));
        jsonObject.put("id", subId);
        return jsonObject.toString();
    }

    /**
     * 转换成交明细订阅
     *
     * @param tradingPair 交易对
     * @return
     */
    static String formatTradeDetailSub(TradingPair tradingPair) {
        String subId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sub", String.format(HuobiQuotationConstant.TRADE_DETAIL_SUB, tradingPair.getTrade()));
        jsonObject.put("id", subId);
        return jsonObject.toString();
    }

    /**
     * 转换为市场概要订阅消息
     *
     * @param tradingPair 交易对
     * @return
     */
    static String formatMarketDetailSub(TradingPair tradingPair) {
        String subId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sub", String.format(HuobiQuotationConstant.MARKET_DETAIL_SUB, tradingPair.getTrade()));
        jsonObject.put("id", subId);
        return jsonObject.toString();
    }

    /**
     * 转换为买一卖一逐笔订阅行情
     *
     * @param tradingPair 交易对
     * @return
     */
    static String formatStrokeQuotesSub(TradingPair tradingPair){
        String subId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sub", String.format(HuobiQuotationConstant.STROKE_QUOTES_SUB, tradingPair.getTrade()));
        jsonObject.put("id", subId);
        return jsonObject.toString();
    }
}
