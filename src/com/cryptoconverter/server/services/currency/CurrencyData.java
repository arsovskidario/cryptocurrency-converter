package com.cryptoconverter.server.services.currency;

public class CurrencyData {

    private String asset_id;
    private String name;
    private int type_is_crypto;
    private String data_start;
    private String data_end;
    private String data_quote_start;
    private String data_quote_end;
    private String data_orderbook_start;
    private String data_orderbook_end;
    private String data_trade_start;
    private String data_trade_end;

    private int data_symbols_count;
    private double volume_1hrs_usd;
    private double volume_1day_usd;
    private double volume_1mth_usd;
    private double price_usd;
    private String id_icon;

    public CurrencyData(String asset_id, String name, int type_is_crypto, String data_start, String data_end,
                        String data_quote_start, String data_quote_end, String data_orderbook_start, String data_orderbook_end,
                        String data_trade_start, String data_trade_end, int data_symbols_count, double volume_1hrs_usd,
                        double volume_1day_usd, double volume_1mth_usd, double price_usd, String id_icon) {
        this.asset_id = asset_id;
        this.name = name;
        this.type_is_crypto = type_is_crypto;
        this.data_start = data_start;
        this.data_end = data_end;
        this.data_quote_start = data_quote_start;
        this.data_quote_end = data_quote_end;
        this.data_orderbook_start = data_orderbook_start;
        this.data_orderbook_end = data_orderbook_end;
        this.data_trade_start = data_trade_start;
        this.data_trade_end = data_trade_end;
        this.data_symbols_count = data_symbols_count;
        this.volume_1hrs_usd = volume_1hrs_usd;
        this.volume_1day_usd = volume_1day_usd;
        this.volume_1mth_usd = volume_1mth_usd;
        this.price_usd = price_usd;
        this.id_icon = id_icon;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType_is_crypto() {
        return type_is_crypto;
    }

    public void setType_is_crypto(int type_is_crypto) {
        this.type_is_crypto = type_is_crypto;
    }

    public String getData_start() {
        return data_start;
    }

    public void setData_start(String data_start) {
        this.data_start = data_start;
    }

    public String getData_end() {
        return data_end;
    }

    public void setData_end(String data_end) {
        this.data_end = data_end;
    }

    public String getData_quote_start() {
        return data_quote_start;
    }

    public void setData_quote_start(String data_quote_start) {
        this.data_quote_start = data_quote_start;
    }

    public String getData_quote_end() {
        return data_quote_end;
    }

    public void setData_quote_end(String data_quote_end) {
        this.data_quote_end = data_quote_end;
    }

    public String getData_orderbook_start() {
        return data_orderbook_start;
    }

    public void setData_orderbook_start(String data_orderbook_start) {
        this.data_orderbook_start = data_orderbook_start;
    }

    public String getData_orderbook_end() {
        return data_orderbook_end;
    }

    public void setData_orderbook_end(String data_orderbook_end) {
        this.data_orderbook_end = data_orderbook_end;
    }

    public String getData_trade_start() {
        return data_trade_start;
    }

    public void setData_trade_start(String data_trade_start) {
        this.data_trade_start = data_trade_start;
    }

    public String getData_trade_end() {
        return data_trade_end;
    }

    public void setData_trade_end(String data_trade_end) {
        this.data_trade_end = data_trade_end;
    }

    public int getData_symbols_count() {
        return data_symbols_count;
    }

    public void setData_symbols_count(int data_symbols_count) {
        this.data_symbols_count = data_symbols_count;
    }

    public double getVolume_1hrs_usd() {
        return volume_1hrs_usd;
    }

    public void setVolume_1hrs_usd(double volume_1hrs_usd) {
        this.volume_1hrs_usd = volume_1hrs_usd;
    }

    public double getVolume_1day_usd() {
        return volume_1day_usd;
    }

    public void setVolume_1day_usd(double volume_1day_usd) {
        this.volume_1day_usd = volume_1day_usd;
    }

    public double getVolume_1mth_usd() {
        return volume_1mth_usd;
    }

    public void setVolume_1mth_usd(double volume_1mth_usd) {
        this.volume_1mth_usd = volume_1mth_usd;
    }

    public double getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(double price_usd) {
        this.price_usd = price_usd;
    }

    public String getId_icon() {
        return id_icon;
    }

    public void setId_icon(String id_icon) {
        this.id_icon = id_icon;
    }

    public boolean isCrypto() {
        return type_is_crypto == 1;
    }
}
