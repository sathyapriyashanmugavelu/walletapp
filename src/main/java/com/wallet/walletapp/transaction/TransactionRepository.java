package com.wallet.walletapp.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(Long walletId);

    @Query(value = "SELECT * FROM transaction WHERE wallet_id = ?1 order by created_at desc LIMIT 5", nativeQuery = true)
    List<Transaction> getRecentByWalletId(Long walletId);

    @Query(value="SELECT * FROM transaction WHERE wallet_id = ?1 AND cast(created_at AS date) BETWEEN ?2 and ?3 order by created_at desc",nativeQuery = true)
    List<Transaction> getFilterTransaction(Long walletId, Date fromDate, Date toDate);
}
