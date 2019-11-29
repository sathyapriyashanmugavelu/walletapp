package com.wallet.walletapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    @Query(value = "SELECT id from usertable WHERE user_name = ?1 ", nativeQuery = true)
    Long findUserId(String username);

}
