package com.cryptoconverter.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CryptoConverterClient {
    private static final String HOST_NAME = "localhost";
    private static final int BUFFER_SIZE = 4096;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    private boolean isRunning;
    private int port;
    private SocketChannel socketChannel;

    public CryptoConverterClient(int port) {
        this.port = port;
        this.isRunning = true;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            connectToServer();
            while (isRunning) {
                String line = scanner.nextLine();
                sendMessageToServer(line);
                printResponseFromServer();
                if (line.matches("^disconnect\\s*")) {
                    socketChannel.close();
                    this.isRunning = false;
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("There is a problem with the network communication" + e);
        }
    }

    private void connectToServer() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(HOST_NAME, port));
        System.out.println("Client was connected to the server : ");
    }

    private void sendMessageToServer(String line) throws IOException {
        buffer.clear();
        buffer.put(line.getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        socketChannel.write(buffer);
    }

    private void printResponseFromServer() throws IOException {

        //TODO: Buffer get ?
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        String reply = new String(byteArray, StandardCharsets.UTF_8); // buffer drain

        System.out.println(reply);

    }


}
