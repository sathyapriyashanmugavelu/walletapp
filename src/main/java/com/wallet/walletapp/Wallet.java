package com.wallet.walletapp;

class Wallet {
    private final String name;
    private final int balance;

    Wallet(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    int balance() {
        return balance;
    }
}
