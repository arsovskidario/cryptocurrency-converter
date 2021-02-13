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

    //TODO: Test sell
    //TODO: Test updateCurrencies



}