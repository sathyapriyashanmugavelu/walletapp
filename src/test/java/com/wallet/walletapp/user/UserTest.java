package com.wallet.walletapp.user;

import com.wallet.walletapp.wallet.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class UserTest {

    @Test
    void shouldCreateWalletForANewUserWithBalanceZero() {
        User user = mock(User.class);
        Wallet wallet = new Wallet(0L);
        wallet.setUser(user);

        assertEquals(0, wallet.getBalance());
    }
}
