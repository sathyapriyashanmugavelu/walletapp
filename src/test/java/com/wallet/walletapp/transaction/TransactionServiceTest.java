package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletRepository;
import com.wallet.walletapp.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class TransactionServiceTest {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void shouldCreateDebitTransaction() throws Exception {
        Wallet savedWallet = walletRepository.save(new Wallet(100L));
        TransactionService transactionService = transactionService();
        Transaction savedTransaction = transactionService.create(debitTransaction(), savedWallet.getId());

//        TODO: Also Assert on wallet.getTransctions()
        assertNotNull(savedTransaction);
        assertEquals(TransactionType.DEBIT, savedTransaction.getTransactionType());
    }

    @Test
    void shouldDebit50IntoTheWallet() throws Exception {
        Wallet savedWallet = walletRepository.save(new Wallet(100L));
        TransactionService transactionService = transactionService();

        transactionService.create(debitTransaction(), savedWallet.getId());

        savedWallet = walletService().fetch(savedWallet.getId());
        assertEquals(150L, savedWallet.getBalance());
    }

    private WalletService walletService() {
        return new WalletService(walletRepository);
    }

    private TransactionService transactionService() {
        return new TransactionService(walletService(), transactionRepository);
    }

    private Transaction debitTransaction() {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(50L);
        newTransaction.setTransactionType(TransactionType.DEBIT);
        return newTransaction;
    }

}