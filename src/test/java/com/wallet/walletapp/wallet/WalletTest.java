package com.wallet.walletapp.wallet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WalletTest {
    @Test
    void expectWalletBalanceToIncreaseBy100OnDeitOf100() {
        Wallet wallet = new Wallet(100L);
        Long debitOf100 = 100L;

        wallet.debit(debitOf100);

        assertEquals(200, wallet.getBalance());
    }

}
