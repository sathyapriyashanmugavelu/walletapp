package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TransactionService {
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

    public List<Transaction> fetch(Long walletId) {
        List<Transaction> transactions=transactionRepository.findByWalletId(walletId);
        return transactions;
    }
}
