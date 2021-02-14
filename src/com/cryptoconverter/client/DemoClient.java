package com.cryptoconverter.client;

public class DemoClient {
    public static void main(String[] args) {
        CryptoConverterClient client = new CryptoConverterClient(4500);
        client.start();
    }
}
