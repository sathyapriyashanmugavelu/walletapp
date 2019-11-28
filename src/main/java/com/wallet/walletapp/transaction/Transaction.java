package com.wallet.walletapp.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wallet.walletapp.wallet.InsufficientBalanceException;
import com.wallet.walletapp.wallet.Wallet;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 10, message = "Amount should not be lesser than 10")
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

    @Transient
    private String createdAtISTFormat;


    public Transaction() {
    }

    public Transaction(@Min(value = 10, message = "Amount should not be lesser than 10") Long amount, String remarks, TransactionType transactionType, Wallet wallet) {
        this.amount = amount;
        this.remarks = remarks;
        this.transactionType = transactionType;
        this.wallet = wallet;
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

    void process() throws InsufficientBalanceException {
        if(transactionType == TransactionType.CREDIT) {
            wallet.credit(amount);
        }
        else if(transactionType == TransactionType.DEBIT) {
            wallet.debit(amount);
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getcreatedAtISTFormat() {
        return this.createdAtISTFormat;
    }

    public void setCreatedAtISTFormat(String createdAtISTFormat) {
        this.createdAtISTFormat = createdAtISTFormat;
    }
}
