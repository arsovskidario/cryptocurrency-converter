package com.cryptoconverter.server;

import org.junit.Test;
import org.mockito.Mock;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandParserTest {

    @Mock
    SocketChannel socketChannel;

    @Mock
    SocketChannel unregisteredChannel;

    @Test
    public void testParseInputInvalidRegister() {
        String expected = "[ Unknown command ]" + System.lineSeparator();
        List<String> list = Arrays.asList("register", "vavava");
        String response = CommandParser.parseInput(list, socketChannel);

        assertEquals(expected, response);
    }

    @Test
    public void testParseInputValidRegistration() {
        String username = "dario";
        String password = "12345";

        String expected = "[ Username " + username + " successfully registered ]" + System.lineSeparator();
        List<String> list = Arrays.asList("register", username, password);

        String response = CommandParser.parseInput(list, socketChannel);

        assertEquals(expected, response);
    }


    @Test
    public void testParseInputLoginNonExistentAccount() {
        String expected = "[ Invalid username/password combination ]" + System.lineSeparator();

        List<String> list = Arrays.asList("login", "marko", "darko");

        String response = CommandParser.parseInput(list, socketChannel);

        assertEquals(expected, response);
    }

    @Test
    public void testParseInputLoginExistentAccount() {
        String username = "ana";
        String password = "openjdk14";

        List<String> list = Arrays.asList("register", username, password);

        CommandParser.parseInput(list, socketChannel);

        List<String> loginList = Arrays.asList("login", username, password);
        String response = CommandParser.parseInput(loginList, socketChannel);

        String expected = "[ User " + username + " successfully logged in ]" + System.lineSeparator();

        assertEquals(expected, response);
    }

    @Test
    public void testParseInputLogoutUser() {
        String username = "ana1";
        String password = "openjdk14";
        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, socketChannel);

        List<String> list2 = Arrays.asList("login", username, password);
        CommandParser.parseInput(list2, socketChannel);

        List<String> logoutList = Arrays.asList("logout");

        String response = CommandParser.parseInput(logoutList, socketChannel);

        String expected = "[ Successfully logged out ]" + System.lineSeparator();

        assertEquals(expected, response);

    }

    @Test
    public void testParseInputLoginNotLoggedIn() {
        String username = "ana12";
        String password = "openjdk14";
        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, unregisteredChannel);

        List<String> list2 = Arrays.asList("login", username, password);
        CommandParser.parseInput(list2, unregisteredChannel);

        List<String> logoutList = Arrays.asList("logout");
        CommandParser.parseInput(logoutList, unregisteredChannel);


        String response = CommandParser.parseInput(logoutList, unregisteredChannel);
        String expected = "[ You are not logged in! ]" + System.lineSeparator();

        assertEquals(expected, response);
    }


    @Test
    public void testParseInputNonAlphaRegister() {
        String username = "Dar@$ko";
        String password = "password";

        List<String> list = Arrays.asList("register", username, password);
        String expected = "[ Username " + username + " is invalid, select a valid one ]" + System.lineSeparator();

        String response = CommandParser.parseInput(list, socketChannel);

        assertEquals(expected, response);
    }

    @Test
    public void testParseInputAlreadyRegisteredUser() {
        String username = "Darko90";
        String password = "password";

        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, socketChannel);

        String expected = "[ Username " + username + " is already taken,"
                + " select another one ]" + System.lineSeparator();

        String response = CommandParser.parseInput(list, socketChannel);

        assertEquals(expected, response);
    }


    @Test
    public void testParseInputDepositMoney() {
        String username = "Darko67";
        String password = "password";

        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, socketChannel);

        List<String> list2 = Arrays.asList("login", username, password);
        CommandParser.parseInput(list2, socketChannel);

        List<String> responseList = Arrays.asList("deposit-money", "100.0");
        String response = CommandParser.parseInput(responseList, socketChannel);
        String expected = "[ Successfully deposited 100.0$ ]" + System.lineSeparator();

        assertEquals(response, expected);


    }

    @Test
    public void testParseInputDepositMoneyInvalidArgs() {
        String username = "Darko78";
        String password = "password";

        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, socketChannel);

        List<String> list2 = Arrays.asList("login", username, password);
        CommandParser.parseInput(list2, socketChannel);

        List<String> responseList = Arrays.asList("deposit-money", "100.0", "$$$$");
        String response = CommandParser.parseInput(responseList, socketChannel);
        String expected = "[ Invalid arguments!] " + System.lineSeparator();

        assertEquals(response, expected);
    }

    @Test
    public void testParseInputDepositMoneyNotLoggedIn() {
        String username = "Darko56";
        String password = "password";

        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, unregisteredChannel);

        List<String> responseList = Arrays.asList("deposit-money", "100.0");
        String response = CommandParser.parseInput(responseList, socketChannel);

        String expected = "[ You are not logged in! ]" + System.lineSeparator();

        assertEquals(expected, response);
    }

    @Test
    public void testParseInputHelpMessage() {
        String expected = "register <username> <password> =>  register to the server" + System.lineSeparator() +
                "login <username> <password> => login to server " + System.lineSeparator() +
                "logout =>  active user can logout" + System.lineSeparator() +
                "deposit-money <amount> => deposit money to your wallet" + System.lineSeparator() +
                "list-offerings => show top 50 offerings for cryptocurrencies" + System.lineSeparator() +
                "get-wallet-summary => shows information about each of your transaction (purchase time, bought price, current price)" + System.lineSeparator() +
                "get-wallet-overall-summary => shows details about the profit/lost based on all transactions" + System.lineSeparator() +
                "buy --offering=<offering_code> --money=<amount>  => buy currency with <offering_code> (asset_id) for <amount> of money" + System.lineSeparator() +
                "sell --offering=<offering_code> --quantity=<amount> => sell <amount> of currency with <offering_code> (asset_id)" + System.lineSeparator() +
                "disconnect => disconnect current session" + System.lineSeparator();

        String response = CommandParser.parseInput(Arrays.asList("help"), socketChannel);

        assertEquals(expected, response);
    }

    @Test
    public void testListOfferingsNotLoggedIn() {
        List<String> list = Arrays.asList("list-offerings");
        String expected = "[ You are not logged in! ]" + System.lineSeparator();
        String response = CommandParser.parseInput(list, socketChannel);
        assertEquals(expected, response);

    }

    @Test
    public void testListOfferingsLoggedInUser() {
        String username = "Darko0";
        String password = "password";

        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, socketChannel);

        List<String> list2 = Arrays.asList("login", username, password);
        CommandParser.parseInput(list2, socketChannel);

        String response = CommandParser.parseInput(Arrays.asList("list-offerings"), socketChannel);

        assertTrue(response.contains("DOGE"));
    }

    //Buy

    @Test
    public void testBuyInvalidArguments() {
        String expected = "[ Invalid arguments!] " + System.lineSeparator();
        String response = CommandParser.parseInput(Arrays.asList("buy", "blablabal"), socketChannel);

        assertEquals(expected, response);
    }

    @Test
    public void testBuyNotLoggedIn() {
        String username = "Darko1";
        String password = "password";

        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, socketChannel);

        String expected = "[ You are not logged in! ]" + System.lineSeparator();
        List<String> buyList = Arrays.asList("buy", "--offering=BTC", "--money=123");
        String response = CommandParser.parseInput(buyList, socketChannel);

        assertEquals(expected, response);
    }

    @Test
    public void testBuySuccess() {
        String username = "Darko12";
        String password = "password";

        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, socketChannel);

        List<String> list2 = Arrays.asList("login", username, password);
        CommandParser.parseInput(list2, socketChannel);

        List<String> depositList = Arrays.asList("deposit-money", "123");
        CommandParser.parseInput(depositList, socketChannel);

        List<String> buyList = Arrays.asList("buy", "--offering=BTC", "--money=123");
        String expected = "[ Successfully bought 123 of BTC ]" + System.lineSeparator();
        String response = CommandParser.parseInput(buyList, socketChannel);


    }

    @Test
    public void testSellInvalidArguments() {
        String expected = "[ Invalid arguments!] " + System.lineSeparator();
        String response = CommandParser.parseInput(Arrays.asList("sell", "blablabal"), socketChannel);

        assertEquals(expected, response);

    }

    @Test
    public void testSellNotLoggedIn() {
        String expected = "[ You are not logged in! ]" + System.lineSeparator();
        CommandParser.parseInput(Arrays.asList("logout"),socketChannel);
        List<String> buyList = Arrays.asList("sell", "--offering=BTC", "--amount=100");
        String response = CommandParser.parseInput(buyList, socketChannel);

        assertEquals(expected, response);
    }

    @Test
    public void testSellSuccess() {
        String username = "anaBanana1";
        String password = "password123";

        List<String> list = Arrays.asList("register", username, password);
        CommandParser.parseInput(list, socketChannel);

        List<String> list2 = Arrays.asList("login", username, password);
        CommandParser.parseInput(list2, socketChannel);

        List<String> depositList = Arrays.asList("deposit-money", "100");
        CommandParser.parseInput(depositList, socketChannel);

        List<String> buyList = Arrays.asList("buy", "--offering=DOGE", "--money=100");
        CommandParser.parseInput(buyList, socketChannel);

        String[] lines = CommandParser.parseInput(Arrays.asList("get-wallet-summary"), socketChannel).split(System.lineSeparator());
        double amountBought = Double.parseDouble(lines[3].split("Amount: ")[1]);

        List<String> sellList = Arrays.asList("sell", "--offering=DOGE", "--amount=" + amountBought);
        String expected = "[ Successfully sold " + amountBought + " of DOGE ]" + System.lineSeparator();
        String response = CommandParser.parseInput(sellList, socketChannel);

        assertEquals(expected, response);

    }
}