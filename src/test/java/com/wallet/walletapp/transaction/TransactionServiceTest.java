package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletRepository;
import com.wallet.walletapp.wallet.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class TransactionServiceTest {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void shouldCreateDebitTransaction() throws Exception {
        Wallet savedWallet = createWallet();
        TransactionService transactionService = transactionService();
        Transaction savedTransaction = transactionService.create(creditTransaction(50L, savedWallet), savedWallet.getId());

//        TODO: Also Assert on wallet.getTransctions()
        assertNotNull(savedTransaction);
        assertEquals(TransactionType.CREDIT, savedTransaction.getTransactionType());
    }

    @Test
    void shouldDebit50IntoTheWallet() throws Exception {
        Wallet savedWallet = createWallet();
        TransactionService transactionService = transactionService();

        transactionService.create(creditTransaction(50L, savedWallet), savedWallet.getId());

        savedWallet = walletService().fetch(savedWallet.getId());
        assertEquals(150L, savedWallet.getBalance());
    }

    @Test
    void fetchATransactions() throws WalletNotFoundException {
        Wallet savedWallet = createWallet();
        TransactionService transactionService = transactionService();
        transactionService.create(creditTransaction(50L, savedWallet),savedWallet.getId());
        List<Transaction> transactions =transactionService.findTransaction(savedWallet.getId());

        assertEquals(50,transactions.get(0).getAmount());
        assertEquals(TransactionType.CREDIT,transactions.get(0).getTransactionType());
    }

    @Test
    void fetchTransactionsList() {
        Wallet savedWallet = createWallet();
        createDebitTransactions(savedWallet);
        List<Transaction> transactions = transactionService().findTransaction(savedWallet.getId());

        assertEquals(50,transactions.get(1).getAmount());
        assertEquals(TransactionType.CREDIT,transactions.get(1).getTransactionType());
        assertEquals(100,transactions.get(0).getAmount());
        assertEquals(TransactionType.CREDIT,transactions.get(0).getTransactionType());
    }

    private WalletService walletService() {
        return new WalletService(walletRepository);
    }

    private TransactionService transactionService() {
        return new TransactionService(walletService(), transactionRepository);
    }

    private Wallet createWallet() {
        return walletRepository.save(new Wallet(100L));
    }

    private void createDebitTransactions(Wallet wallet) {
        transactionRepository.save(creditTransaction(50L, wallet));
        transactionRepository.save(creditTransaction(100L, wallet));
    }

    private Transaction creditTransaction(Long amount, Wallet wallet) {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(amount);
        newTransaction.setTransactionType(TransactionType.CREDIT);
        newTransaction.setWallet(wallet);
        return newTransaction;
    }
}