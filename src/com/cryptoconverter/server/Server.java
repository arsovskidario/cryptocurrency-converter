package com.cryptoconverter.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Server {
    //TODO: Save user data to File
    // Save CurrencyUpdate and time modified to file
    private int port;
    private static final String HOST_NAME = "localhost";


    private boolean isRunning;
    private Selector selector;

    private static final int BUFFER_SIZE = 4096;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public Server(int port) {
        this.port = port;
        this.isRunning = true;

    }

    public void start() {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();) {
            serverChannel.bind(new InetSocketAddress(HOST_NAME, port));
            serverChannel.configureBlocking(false);

            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server started...");
            while (isRunning) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                //Get selector keys and iterate over them

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    // Register client
                    if (key.isAcceptable()) {

                        //Get server channel that is linked to selector
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();

                        //Server accepts connection and that connection is registered in selector
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        buffer.clear();
                        int rd = clientChannel.read(buffer);
                        if (rd < 0) {
                            System.out.println("[ Client stopped communication ]");
                            ClientManager.disconnectUser(clientChannel);
                            continue;
                        }

                        buffer.flip(); // ready for read


                        // Convert buffer to string

                        byte[] byteArray = new byte[buffer.remaining()];
                        buffer.get(byteArray);
                        String clientReply = new String(byteArray, StandardCharsets.UTF_8);
                        clientReply = clientReply.replace(System.lineSeparator(), "");

                        System.out.println("Received " + clientReply);

                        List<String> tokenizedBuffer = Arrays.asList(clientReply.split(" "));

                        String serverResponse = CommandParser.parseInput(tokenizedBuffer, clientChannel);

                        System.out.println("Will send " + serverResponse);

                        buffer.clear();
                        buffer.put(serverResponse.getBytes(StandardCharsets.UTF_8));

                        buffer.flip();
                        clientChannel.write(buffer);

                        if (tokenizedBuffer.get(0).contains("disconnect")) {
                            ClientManager.disconnectUser(clientChannel);
                        }

                    }

                    iterator.remove();
                }
            }

        } catch (IOException e) {
            System.out.println("Server crashed " + e);
        }

    }

    public void stop() {
        this.isRunning = false;
        try {
            selector.close();
        } catch (IOException e) {
            System.out.println("Failed to close selector " + e);
        }
    }


}
