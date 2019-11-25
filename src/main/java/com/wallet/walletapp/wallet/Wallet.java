package com.wallet.walletapp.wallet;

import com.wallet.walletapp.transaction.Transaction;
import com.wallet.walletapp.user.User;

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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Wallet() {
    }

    public Wallet(Long balance) {
        this.balance = balance;
    }

    public Wallet(long id,long balance) {
    }

    public Long getBalance() {
        return balance;
    }

    public Long getId() {
        return id;
    }

    public void credit(Long amount) {
        balance += amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
