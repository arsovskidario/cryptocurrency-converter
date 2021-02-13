package com.cryptoconverter.services.wallet;

import com.cryptoconverter.services.exceptions.CurrencyNotPresentException;
import com.cryptoconverter.services.exceptions.InsufficientCashForPurchaseException;
import com.cryptoconverter.services.exceptions.InvalidDepositAmount;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class VirtualWalletTest {

    static VirtualWallet wallet = new VirtualWallet();


    @After
    public void resetWallet() {
        double amount = wallet.getWalletCash();
        wallet.withdrawCash(amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawCashInvalidArgs() {
        wallet.withdrawCash(-2);
        wallet.withdrawCash(20);
    }

    @Test
    public void testWithdrawCashValidInput() {
        wallet.depositCash(10.0);
        wallet.withdrawCash(10.0);

        assertTrue(Double.compare(0.0, wallet.getWalletCash()) == 0);

    }


    @Test(expected = InvalidDepositAmount.class)
    public void testDepositInvalidCash() {
        wallet.depositCash(-12);
    }

    @Test
    public void testDepositValidCash() {
        wallet.depositCash(22.0);

        assertTrue(Double.compare(22.0, wallet.getWalletCash()) == 0);
    }

    @Test
    public void testListOfferings() {
        String result = wallet.listOfferings();
        assertTrue(result.contains("BTC"));
    }

    @Test(expected = InvalidDepositAmount.class)
    public void testBuyCurrencyInvalidAmount() {
        wallet.buyCurrency(-1, "BTC");

    }

    @Test(expected = CurrencyNotPresentException.class)
    public void testBuyCurrencyInvalidCurrencyName() {
        wallet.depositCash(12.0);
        wallet.buyCurrency(12.0, "HESOYAM");
    }


    @Test(expected = InsufficientCashForPurchaseException.class)
    public void testBuyCurrencyInsufficientCashAmount() {
        wallet.depositCash(100.0);
        wallet.buyCurrency(200.0, "BTC");
    }

    @Test
    public void testBuyCurrencyValidAmount() {
        wallet.depositCash(200.0);
        wallet.buyCurrency(200.0, "BTC");

        assertTrue(wallet.getWalletSummary().contains("Currency name: Bitcoin"));

    }


    @Test(expected = IllegalArgumentException.class)
    public void testSellCurrencyInvalidAmount() {
        wallet.sellCurrency(-23.0, "BTC");
    }

    @Test(expected = CurrencyNotPresentException.class)
    public void testSellCurrencyInvalidCurrencyName() {
        wallet.sellCurrency(10, "DOGARIOUS");
    }

    @Test
    public void testSellCurrencyValidOperation() {

        wallet.depositCash(200.0);
        wallet.buyCurrency(200.0, "BTC");


        String[] lines = wallet.getWalletSummary().split(System.lineSeparator());
        double amountBought = Double.parseDouble(lines[3].split("Amount: ")[1]);
        double resultAmount = amountBought / 2;
        wallet.sellCurrency(resultAmount, "BTC");

        lines = wallet.getWalletSummary().split(System.lineSeparator());
        double amountLeft = Double.parseDouble(lines[3].split("Amount: ")[1]);


        assertEquals(0, Double.compare(amountLeft, resultAmount));

    }

    @Test
    public void testSellCurrencyFullAmount() {
        wallet.depositCash(150);
        wallet.buyCurrency(75, "BTC");
        wallet.buyCurrency(75, "BTC");

        wallet.sellCurrency(1, "BTC");


        String[] lines = wallet.getWalletSummary().split(System.lineSeparator());

        assertEquals(1, lines.length);
    }


    @Test
    public void testGetWalletOVerallSummary() {
        wallet.depositCash(150);
        wallet.buyCurrency(75, "BTC");
        String[] lines = wallet.getWalletOverallSummary().split(System.lineSeparator());

        assertTrue(lines[1].contains("Cash balance: 75"));
        assertTrue(lines[3].contains("Currency name: Bitcoin"));
    }

}