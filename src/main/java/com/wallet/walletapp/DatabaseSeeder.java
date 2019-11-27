package com.wallet.walletapp;

import com.wallet.walletapp.user.User;
import com.wallet.walletapp.user.UserRepository;
import com.wallet.walletapp.user.UserService;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseSeeder {

    @Autowired
    private Environment environment;

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, UserService userService) {
        return args -> {
            String[] activeProfiles = this.environment.getActiveProfiles();
            if (activeProfiles.length != 0 && activeProfiles[0] != "prod") {
                if (repository.findByUserName("seed-user-1").isEmpty()) {
                    userService.create(new User("seed-user-1", "foobar"));
                }
                if (repository.findByUserName("seed-user-3").isEmpty()) {
                    userService.create(new User("seed-user-3", "foobar"));
                }
            }
        };
    }
}