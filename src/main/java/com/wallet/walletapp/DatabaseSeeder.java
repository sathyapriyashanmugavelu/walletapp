package com.wallet.walletapp;

import com.wallet.walletapp.user.User;
import com.wallet.walletapp.user.UserRepository;
import com.wallet.walletapp.user.UserService;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {
    @Bean
    CommandLineRunner initDatabase(UserRepository repository, UserService userService) {
        return args -> {
            if (repository.findByUserName("seed-user-1").isEmpty()) {
                userService.create(new User("seed-user-1", "foobar"));

            }
            if (repository.findByUserName("seed-user-2").isEmpty()) {
                userService.create(new User("seed-user-2", "foobar"));
            }
        };
    }
}