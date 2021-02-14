package com.cryptoconverter.server;

public class DemoServer {
    public static void main(String[] args) {
        Server server = new Server(4500);
        server.start();
    }
}
