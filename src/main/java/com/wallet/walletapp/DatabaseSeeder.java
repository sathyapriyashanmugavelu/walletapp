package com.wallet.walletapp;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {
    @Bean
    CommandLineRunner initDatabase(WalletRepository repository) {
        return args -> {
            repository.save(new Wallet(100L));
            repository.save(new Wallet(200L));
        };
    }
}