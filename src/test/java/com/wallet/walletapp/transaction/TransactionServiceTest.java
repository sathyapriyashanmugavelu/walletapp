package com.wallet.walletapp.transaction;

import com.wallet.walletapp.user.User;
import com.wallet.walletapp.user.UserRepository;
import com.wallet.walletapp.wallet.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    @Autowired
    UserRepository userRepository;


    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void shouldCreateCreditTransaction() throws Exception {
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
    void fetchATransactions() throws WalletNotFoundException, InsufficientBalanceException {
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
        createCreditTransactions(savedWallet);
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

    private void createCreditTransactions(Wallet wallet) {
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
        assertEquals("25 Nov 2019 05:30:00", formatDate);
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
    @Test
    void shouldBeAbleToTransferMoneyFromOneUserToOther() throws InsufficientBalanceException {

        Wallet fromWallet = new Wallet(500L);
        User fromUser = new User(1L, "foo", "foobar");
        fromWallet.setUser(fromUser);
        userRepository.save(fromUser);
        walletRepository.save(fromWallet);

        Wallet toWallet = new Wallet(100L);
        User toUser = new User(2L, "bar", "foobar");
        toWallet.setUser(toUser);
        userRepository.save(toUser);
        walletRepository.save(toWallet);

        TransactionService transactionService = transactionService();
        Long fromUserId = 1L;
        Long toUserId = 2L;
        Long transferAmount = 100L;
        String remarks = "Test transfer";
        transactionService.transferMoney(fromUserId, toUserId, transferAmount, remarks);

        Wallet fromWalletAfter = walletRepository.findByUserId(1L);
        Wallet toWalletAfter = walletRepository.findByUserId(2L);

        assertEquals(200L, toWalletAfter.getBalance());
        assertEquals(400L, fromWalletAfter.getBalance());

    }
}