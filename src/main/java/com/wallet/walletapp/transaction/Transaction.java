package com.wallet.walletapp.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wallet.walletapp.wallet.Wallet;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
    private String remarks;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private Wallet wallet;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;


    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public Long getAmount() {
        return amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    void process() {
        wallet.debit(amount);
    }
}
