package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletRepository;
import com.wallet.walletapp.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        WalletService walletService = new WalletService(walletRepository);
        TransactionService transactionService = new TransactionService(walletService, transactionRepository);
        Transaction newTransaction = new Transaction();
        Transaction savedTransaction = transactionService.create(newTransaction, savedWallet.getId());

        assertNotNull(savedTransaction);
    }
}