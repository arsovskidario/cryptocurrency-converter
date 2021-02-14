package com.cryptoconverter.server;

import com.cryptoconverter.server.services.wallet.VirtualWallet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

class ClientManager {
    private static Map<String, String> credentials = new HashMap<>();
    private static Map<String, VirtualWallet> userWallets = new HashMap<>();
    private static Map<SocketChannel, String> loggedInUsers = new HashMap<>();


    private static boolean isAlphaNumeric(String word) {
        return word.matches("^[a-zA-Z0-9\\-\\_\\.]+");
    }

    /**
     * Registers user to the platform, performing checks for correct username
     * and if he's already registered
     *
     * @param username
     * @param password
     * @return server response
     */
    public static String registerUser(String username, String password) {
        String serverResponse;
        if (!isAlphaNumeric(username)) {
            serverResponse = "[ Username " + username + " is invalid, select a valid one ]";
        } else {
            if (credentials.containsKey(username)) {
                serverResponse = "[ Username " + username + " is already taken,"
                        + " select another one ]";
            } else {
                credentials.put(username, password);
                userWallets.put(username, new VirtualWallet());
                serverResponse = "[ Username " + username + " successfully registered ]";
            }
        }

        return serverResponse;
    }

    /**
     * Checks if user is registered on the platform and creates a session for him.
     *
     * @param username
     * @param password
     * @param clientChannel
     * @return server response
     */

    public static String logInUser(String username, String password, SocketChannel clientChannel) {
        String serverResponse;
        if (!credentials.containsKey(username) || !credentials.get(username).equals(password)) {
            serverResponse = "[ Invalid username/password combination ]";
        } else {
            loggedInUsers.put(clientChannel, username);
            serverResponse = "[ User " + username + " successfully logged in ]";

        }

        return serverResponse;
    }


    /**
     * Checks if the user is logged in to the server
     *
     * @param channel
     * @return boolean indicating if the user is logged and can use other commands
     */
    public static boolean hasSession(SocketChannel channel) {
        return loggedInUsers.containsKey(channel);
    }

    public static String logoutUser(SocketChannel channel) {
        loggedInUsers.remove(channel);
        return "[ Successfully logged out ]";
    }


    //////////////////////
    // User is loged in below methods


    public static String depositMoney(SocketChannel channel, double cashAmount) {
        VirtualWallet wallet = getUserWallet(channel);
        wallet.depositCash(cashAmount);

        return "[ Successfully deposited " + cashAmount + "$ ]";

    }

    public static String buyCurrency(SocketChannel channel, String name, double amount) {
        VirtualWallet wallet = getUserWallet(channel);
        wallet.buyCurrency(amount, name);

        return "[ Successfully bought " + amount + " of " + name + " ]";
    }

    public static String sellCurrency(SocketChannel channel, String name, double amount) {
        VirtualWallet wallet = getUserWallet(channel);
        wallet.sellCurrency(amount, name);

        return "[ Successfully sold " + amount + " of " + name + " ]";
    }

    public static String listOfferings(SocketChannel channel) {
        VirtualWallet wallet = getUserWallet(channel);
        return wallet.listOfferings();

    }

    public static String getWalletSummary(SocketChannel channel) {
        VirtualWallet wallet = getUserWallet(channel);
        return wallet.getWalletSummary();
    }


    public static String getWalletOverallSummary(SocketChannel channel) {
        VirtualWallet wallet = getUserWallet(channel);
        return wallet.getWalletOverallSummary();
    }

    public static void disconnectUser(SocketChannel channel) throws IOException {
        loggedInUsers.remove(channel);
        channel.close();
    }

    private static VirtualWallet getUserWallet(SocketChannel channel) {
        String username = loggedInUsers.get(channel);
        return userWallets.get(username);
    }


}
