package com.wallet.walletapp.wallet;

import com.wallet.walletapp.transaction.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long balance;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public Wallet() {
    }

    public Wallet(Long balance) {
        this.balance = balance;
    }

    public Long getBalance() {
        return balance;
    }

    public Long getId() {
        return id;
    }

    public void debit(Long amount) {
        balance += amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
