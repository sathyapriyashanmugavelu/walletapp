package com.wallet.walletapp.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(Long walletId);

    @Query(value = "SELECT * FROM transaction WHERE wallet_id = ?1 LIMIT 5", nativeQuery = true)
    List<Transaction> getRecentByWalletId(Long walletId);
}
