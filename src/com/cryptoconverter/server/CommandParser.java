package com.cryptoconverter.server;

import java.nio.channels.SocketChannel;
import java.util.List;

class CommandParser {
    private static final int REGISTER_PARAMS_LIMIT = 3;
    private static final int LOGIN_PARAMS_LIMIT = 3;
    private static final int DEPOSIT_MONEY_PARAMS_LIMIT = 2;
    private static final int BUY_PARAMS_LIMIT = 3;
    private static final int SELL_PARAMS_LIMIT = 3;

    private static final String helpMessage = "register <username> <password> =>  register to the server" + System.lineSeparator() +
            "login <username> <password> => login to server " + System.lineSeparator() +
            "logout =>  active user can logout" + System.lineSeparator() +
            "deposit-money <amount> => deposit money to your wallet" + System.lineSeparator() +
            "list-offerings => show top 50 offerings for cryptocurrencies" + System.lineSeparator() +
            "get-wallet-summary => shows information about each of your transaction (purchase time, bought price, current price)" + System.lineSeparator() +
            "get-wallet-overall-summary => shows details about the profit/lost based on all transactions" + System.lineSeparator() +
            "buy --offering=<offering_code> --money=<amount>  => buy currency with <offering_code> (asset_id) for <amount> of money" + System.lineSeparator() +
            "sell --offering=<offering_code> --quantity=<amount> => sell <amount> of currency with <offering_code> (asset_id)" + System.lineSeparator() +
            "disconnect => disconnect current session";


    public static String parseInput(List<String> input, SocketChannel channel) {
        String serverResponse = "[ Unknown command ]";

        int inputSize = input.size();
        String commandName = input.get(0).toLowerCase();
        switch (commandName) {
            case "help" -> {
                serverResponse = helpMessage;
            }

            case "register" -> {
                if (inputSize == REGISTER_PARAMS_LIMIT) {
                    serverResponse = ClientManager.registerUser(input.get(1), input.get(2));
                }
            }
            case "login" -> {
                if (inputSize == LOGIN_PARAMS_LIMIT) {
                    serverResponse = ClientManager.logInUser(input.get(1), input.get(2), channel);
                }
            }
            case "logout" -> {
                if (ClientManager.hasSession(channel)) {
                    serverResponse = ClientManager.logoutUser(channel);
                } else {
                    serverResponse = "[ You are not logged in! ]";
                }

            }

            case "deposit-money" -> {
                if (inputSize != DEPOSIT_MONEY_PARAMS_LIMIT) {
                    serverResponse = "[ Invalid arguments!] ";
                    break;
                }


                if (ClientManager.hasSession(channel)) {
                    serverResponse = ClientManager.depositMoney(channel, Double.parseDouble(input.get(1)));
                } else {
                    serverResponse = "[ You are not logged in! ]";
                }
            }
            case "list-offerings" -> {
                if (ClientManager.hasSession(channel)) {
                    serverResponse = ClientManager.listOfferings(channel);
                } else {
                    serverResponse = "[ You are not logged in! ]";
                }
            }
            case "get-wallet-summary" -> {
                if (ClientManager.hasSession(channel)) {
                    serverResponse = ClientManager.getWalletSummary(channel);
                } else {
                    serverResponse = "[ You are not logged in! ]";
                }
            }
            case "get-wallet-overall-summary" -> {
                if (ClientManager.hasSession(channel)) {
                    serverResponse = ClientManager.getWalletOverallSummary(channel);
                } else {
                    serverResponse = "[ You are not logged in! ]";
                }
            }

            case "buy" -> {
                if (inputSize != BUY_PARAMS_LIMIT) {
                    serverResponse = "[ Invalid arguments!] ";
                    break;
                }

                if (ClientManager.hasSession(channel)) {
                    String name = input.get(1).split("=")[1];
                    double amount = Double.parseDouble(input.get(2).split("=")[1]);
                    serverResponse = ClientManager.buyCurrency(channel, name, amount);
                } else {
                    serverResponse = "[ You are not logged in! ]";
                }

            }
            case "sell" -> {
                if (inputSize != SELL_PARAMS_LIMIT) {
                    serverResponse = "[ Invalid arguments!] ";
                    break;
                }

                if (ClientManager.hasSession(channel)) {
                    String name = input.get(1).split("=")[1];
                    double amount = Double.parseDouble(input.get(2).split("=")[1]);
                    serverResponse = ClientManager.sellCurrency(channel, name, amount);
                } else {
                    serverResponse = "[ You are not logged in! ]";
                }

            }
            case "disconnect" -> {
                return "[ Disconnected from server ]";
            }
            default -> {
                break;
            }

        }


        return serverResponse + System.lineSeparator();
    }

}
