package com.wallet.walletapp.wallet;

import com.wallet.walletapp.transaction.*;
import com.wallet.walletapp.user.User;
import com.wallet.walletapp.user.UserService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(WalletController.class)
@WithMockUser
class WalletControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WalletService walletService;

    @MockBean
    TransactionService transactionService;

    @MockBean
    UserService userService;

    @Test
    void shouldGetWalletForUser() throws Exception {
        User user = new User(1,"test-user,", "test1234");
        Wallet wallet = new Wallet(1,0);
        wallet.setUser(user);
        List<Transaction> recentTransactions = new ArrayList<>();
        when(userService.getCurrentUserId()).thenReturn(user.getId());
        when(walletService.findWalletForUser(anyLong())).thenReturn(wallet);
        when(transactionService.getRecentTransactions((long) 1)).thenReturn(recentTransactions);
        mockMvc.perform(get("/wallet"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("wallet", wallet))
                .andExpect(view().name("wallets/show"));
    }

    @Test
    void shouldShowRecentTransactionsInDashboad() throws Exception  {
        User user = new User(1,"test-user,", "test1234");
        Wallet wallet = new Wallet(1,0);
        wallet.setUser(user);
        when(userService.getCurrentUserId()).thenReturn(user.getId());
        when(walletService.findWalletForUser(1L)).thenReturn(wallet);
        List<Transaction> recentTransactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setId(Long.valueOf(1));
        transaction1.setAmount(Long.valueOf(100));
        transaction1.setRemarks("test");
        transaction1.setTransactionType(TransactionType.CREDIT);
        transaction1.setWallet(wallet);
        recentTransactions.add(transaction1);

        when(transactionService.getRecentTransactions(anyLong())).thenReturn(recentTransactions);

        mockMvc.perform(get("/wallet"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("recentTransactions", Matchers.equalTo(recentTransactions)))
                .andExpect(view().name("wallets/show"));

    }
}