package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.Wallet;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TransactionTest {
    @Test
    void shouldUpdateWalletBalanceOnProcess() {
        Wallet wallet = mock(Wallet.class);
        Transaction transaction = debitTransaction();
        transaction.setWallet(wallet);

        transaction.process();

        verify(wallet).debit(50L);
    }

    private Transaction debitTransaction() {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(50L);
        newTransaction.setTransactionType(TransactionType.DEBIT);
        return newTransaction;
    }
}
