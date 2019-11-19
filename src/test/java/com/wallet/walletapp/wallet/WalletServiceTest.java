package com.wallet.walletapp.wallet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WalletServiceTest {
    @Autowired
    WalletRepository walletRepository;

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
}