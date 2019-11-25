package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletRepository;
import com.wallet.walletapp.wallet.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    void shouldReturnTheRecent5TransactionsOnly(){
        Wallet wallet = createWallet();
        transactionRepository.save(creditTransaction(100L, wallet));
        transactionRepository.save(creditTransaction(200L, wallet));
        transactionRepository.save(creditTransaction(300L, wallet));
        transactionRepository.save(creditTransaction(400L, wallet));
        transactionRepository.save(creditTransaction(500L, wallet));
        transactionRepository.save(creditTransaction(600L, wallet));

        TransactionService transactionService = transactionService();
        List<Transaction> recentTransactions = transactionService.getRecentTransactions(wallet.getId());
        assertEquals(5, recentTransactions.size());

        // for(Transaction transaction : recentTransactions){
        //     if(transaction.getAmount() == 100L)
        //     {
        //         fail();
        //     }
        // }

    }

    @Test
    void shouldSetDateFormat() throws Exception {
        Wallet wallet = createWallet();
        List<Transaction> transactions = new ArrayList<>();
        Transaction testTransaction = creditTransaction(50L, wallet);
        testTransaction.setCreatedAt(new Date(Date.UTC(119, 10, 25, 0, 0, 0)));
        transactions.add(testTransaction);

        TransactionService transactionService = transactionService();
        List<Transaction> transactionWithDateFormat = transactionService.addISTDateFormat(transactions);

        String formatDate =  transactionWithDateFormat.get(0).getcreatedAtISTFormat();
        assertEquals("Nov 25 2019T05:30:00 IST", formatDate);
    }

    @Test
    void shouldSortTransactions(){

        Wallet wallet = createWallet();

        List<Transaction> transactions = new ArrayList<>();
        Transaction testTransaction1 = creditTransaction(100L, wallet);
        testTransaction1.setCreatedAt(new Date(119, 10, 22));
        transactions.add(testTransaction1);

        Transaction testTransaction2 = creditTransaction(200L, wallet);
        testTransaction2.setCreatedAt(new Date(119, 10, 25));
        transactions.add(testTransaction2);

        TransactionService transactionService = transactionService();
        List<Transaction> sortedTransactions = transactionService.sortTransactions(transactions);

        assertEquals(200L, sortedTransactions.get(0).getAmount());
        assertEquals(100L, sortedTransactions.get(1).getAmount());

    }

}