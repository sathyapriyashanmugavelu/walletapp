package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Transaction create(Transaction transaction, Long walletId) throws WalletNotFoundException {
        Wallet wallet = walletService.fetch(walletId);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setWallet(wallet);
        transaction.process();
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findTransaction(Long walletId) {
        List<Transaction> transactions=transactionRepository.findByWalletId(walletId);
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

    public List<Transaction> sortTransactions(List<Transaction> transactions)
    {
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
}
