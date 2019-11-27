package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.InsufficientBalanceException;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class TransactionService {
    @Autowired
    WalletService walletService;

    @Autowired
    TransactionRepository transactionRepository;

    public TransactionService(WalletService walletService, TransactionRepository transactionRepository) {
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
    }

    public Transaction create(Transaction transaction, Long walletId) throws WalletNotFoundException, InsufficientBalanceException {
        Wallet wallet = walletService.fetch(walletId);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setWallet(wallet);
        transaction.process();
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findTransaction(Long walletId) {
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        List<Transaction> transactionsWithDateFormat = addISTDateFormat(transactions);
        List<Transaction> sortedTransactions = sortTransactions(transactionsWithDateFormat);
        return sortedTransactions;
    }

    public List<Transaction> getRecentTransactions(Long walletId) {
        List<Transaction> transactions = transactionRepository.getRecentByWalletId(walletId);
        List<Transaction> transactionsWithDateFormat = addISTDateFormat(transactions);
        List<Transaction> sortedTransactions = sortTransactions(transactionsWithDateFormat);
        return sortedTransactions;
    }

    public List<Transaction> sortTransactions(List<Transaction> transactions) {
        transactions.sort(Comparator.comparing(Transaction::getCreatedAt).reversed());
        return transactions;
    }

    public List<Transaction> addISTDateFormat(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            Date createdAt = transaction.getCreatedAt();
            String istFormat = formatTime(createdAt);
            transaction.setCreatedAtISTFormat(istFormat);
        }
        return transactions;
    }

    private static String formatTime(Date date) {
        Calendar ist = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        ist.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        sdf.setCalendar(ist);
        return sdf.format(ist.getTime());
    }

    public void transferMoney(Long fromUserId, Long toUserId, Long transferAmount, String remarks) throws InsufficientBalanceException {
        Wallet fromWallet = walletService.findWalletForUser(fromUserId);
        Transaction fromTransction = new Transaction();
        fromTransction.setWallet(fromWallet);
        fromTransction.setAmount(transferAmount);
        fromTransction.setTransactionType(TransactionType.DEBIT);
        fromTransction.setRemarks(remarks);
        fromTransction.process();

        Wallet toWallet = walletService.findWalletForUser(toUserId);
        Transaction toTransaction = new Transaction();
        toTransaction.setWallet(toWallet);
        toTransaction.setAmount(transferAmount);
        toTransaction.setTransactionType(TransactionType.CREDIT);
        toTransaction.setRemarks(remarks);
        toTransaction.process();

        transactionRepository.save(fromTransction);
        transactionRepository.save(toTransaction);

    }

    public List<Transaction> getFilteredTransactions(Long walletId, String fromDate, String toDate) {
        Date fromDateValue = dateFormat(fromDate, "00:00:00");
        Date toDateValue = dateFormat(toDate, "23:59:59");

        List<Transaction> transactions = transactionRepository.getFilterTransaction(walletId, fromDateValue, toDateValue);
        List<Transaction> transactionsWithDateFormat = addISTDateFormat(transactions);
        List<Transaction> sortedTransactions = sortTransactions(transactionsWithDateFormat);
        return sortedTransactions;
    }

    private Date dateFormat(String dateValue, String time) {
        Date convertedDate = null;
        try {
            convertedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateValue + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}
