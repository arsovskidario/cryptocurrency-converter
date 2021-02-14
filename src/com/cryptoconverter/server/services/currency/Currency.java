package com.cryptoconverter.server.services.currency;

public class Currency {

    private String assetId;
    private String name;
    private Double currentPrice;

    public Currency(String assetId, String name, Double currentPrice) {
        this.assetId = assetId;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public void updatePrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getName() {
        return name;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }
}
