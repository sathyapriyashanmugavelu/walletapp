package com.wallet.walletapp.wallet;

import com.wallet.walletapp.user.User;
import com.wallet.walletapp.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WalletServiceTest {
    @Autowired
    WalletRepository walletRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        walletRepository.deleteAll();
    }

    @Test
    void fetchAWallet() throws Exception {
        Wallet savedWallet = walletRepository.save(new Wallet(100L));
        WalletService walletService = new WalletService(walletRepository);

        Wallet wallet = walletService.fetch(savedWallet.getId());

        assertEquals(100L, wallet.getBalance());
    }

    @Test
    void failsToFetchAWalletWithInvalidId() {
        long invalidId = 999L;
        WalletService walletService = new WalletService(walletRepository);

        assertThrows(WalletNotFoundException.class, () -> walletService.fetch(invalidId));
    }

    @Test
    void shouldCreateAWallet() {
        WalletService walletService = new WalletService(walletRepository);

        Wallet savedWallet = walletService.create(new Wallet(100L));

        assertNotNull(savedWallet);
        assertNotNull(savedWallet.getId());
    }

    @Test
    void shouldGetWalletForAUser() {
        User user = new User("test-user", "test1234");
        userRepository.save(user);
        Wallet savedWallet = walletRepository.save(new Wallet(100L));
        savedWallet.setUser(user);
        WalletService walletService = new WalletService(walletRepository);

        assertEquals(savedWallet, walletService.findWalletForUser(user.getId()));
    }
}