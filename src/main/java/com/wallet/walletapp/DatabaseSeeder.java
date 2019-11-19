package com.wallet.walletapp;

import com.wallet.walletapp.user.User;
import com.wallet.walletapp.user.UserRepository;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {
    @Bean
    CommandLineRunner initDatabase(UserRepository repository, WalletService walletService) {
        return args -> {
            if (repository.findByUserName("seed-user-1").isEmpty()) {
                User savedUser = repository.save(new User("seed-user-1", "foobar"));
                Wallet wallet = new Wallet(0L);
                wallet.setUser(savedUser);
                walletService.create(wallet);
            }
            if (repository.findByUserName("seed-user-2").isEmpty()) {
                User savedUser = repository.save(new User("seed-user-2", "foobar"));
                Wallet wallet = new Wallet(0L);
                wallet.setUser(savedUser);
                walletService.create(wallet);
            }
        };
    }
}