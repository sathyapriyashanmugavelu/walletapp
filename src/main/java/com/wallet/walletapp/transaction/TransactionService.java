package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setWallet(wallet);
        transaction.process();
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findTransaction(Long walletId) {
        List<Transaction> transactions=transactionRepository.findByWalletId(walletId);
        transactions.sort(Comparator.comparing(Transaction::getCreatedAt).reversed());
        return transactions;
    }

    public List<Transaction> getRecentTransactions(Long walletId) {
        List<Transaction> transactions = transactionRepository.getRecentByWalletId(walletId);
        transactions.sort(Comparator.comparing(Transaction::getCreatedAt).reversed());
        return transactions;
    }
}
