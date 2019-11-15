package com.wallet.walletapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface WalletRepository extends JpaRepository<Wallet, Long> {
}
