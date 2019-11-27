package com.wallet.walletapp.transaction;

import com.wallet.walletapp.user.UserService;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@WithMockUser
class TransactionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    @MockBean
    UserService userService;

    @MockBean
    WalletService walletService;

    @Test
    void shouldShowNewTransaction() throws Exception {
        Wallet wallet = new Wallet(1,0);
        when(walletService.findWalletForUser(anyLong())).thenReturn(wallet);
        mockMvc.perform(post("/dashboard/transactions/new").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("transaction", isA(Transaction.class)))
                .andExpect(view().name("transactions/new"));
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        Wallet wallet = new Wallet(1,0);
        when(walletService.findWalletForUser(anyLong())).thenReturn(wallet);
        mockMvc.perform(post("/dashboard/transactions").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "100")
                .param("remarks", "rent")
                .param("submit", "Submit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dashboard"));

        ArgumentCaptor<Transaction> argument = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionService).create(argument.capture(), eq(1L));
        assertEquals("rent", argument.getValue().getRemarks());
        assertEquals(100L, argument.getValue().getAmount());
    }

    @Test
    void shoudRedirectWhenCancelled() throws Exception {
        Wallet wallet = new Wallet(1,0);
        when(walletService.findWalletForUser(anyLong())).thenReturn(wallet);
        mockMvc.perform(post("/dashboard/transactions").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "100")
                .param("remarks", "rent")
                .param("cancel", "Cancel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dashboard"));
    }

    @Test
    void shouldShowViewAllTransactions() throws Exception {
        Wallet wallet = new Wallet(1,0);
        when(walletService.findWalletForUser(anyLong())).thenReturn(wallet);
        mockMvc.perform(post("/dashboard/transactions").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transactions/show"));
    }
}
