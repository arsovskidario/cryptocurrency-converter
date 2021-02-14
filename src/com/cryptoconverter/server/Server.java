package com.cryptoconverter.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.Arrays;

import java.util.List;

public class Server {
    //TODO: Responsible for running server and parsing commands

    private int port;
    private static final String HOST_NAME = "localhost";


    private boolean isRunning;
    private Selector selector;

    private static final int BUFFER_SIZE = 1024;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public Server(int port) {
        this.port = port;
        this.isRunning = true;

    }

    public void start() {
        //TODO: Handle disconnect client here

    /*    protected void disconnectClient(ByteBuffer buffer, SocketChannel clientChannel){

            String serverResponse = "[ Disconnected from server ]";


            serverResponse = serverResponse + System.lineSeparator();
            try {
                buffer.clear();
                buffer.put(serverResponse.getBytes("UTF-8"));
                buffer.flip();
                clientChannel.write(buffer);
                // If user forgets to logout log him out
                loggedInChannels.remove(clientChannel);
                clientChannel.close();
            } catch (IOException e) {
                System.out.println("Failed to close client socked");
                e.printStackTrace();
            }*/
        }

    public void stop() {
        this.isRunning = false;
        try {
            selector.close();
        } catch (IOException e) {
            System.out.println("Failed to close selector " + e);
        }
    }

    private void parseInput(String input) {
        List<String> commands = Arrays.asList(input.split(" "));
        String commandName = commands.get(0).toLowerCase();
        switch (commandName) {
            case "register" -> registerUser(input);
            case "login" -> loginUser(input);
            case "help" -> displayHelp();
            case "deposit-money" ->  // Has to be logedin first
            case "list-offerings" -> ;
            case "buy" -> ;
            case "sell" -> ;
            case "get-wallet-summary"
                ;
            case "get-wallet-overall-summary"
                ;
                case "disconnect" -> ;
                default -> break;

        }
    }
}
