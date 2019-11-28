package com.wallet.walletapp.transaction;

import com.wallet.walletapp.user.UserService;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.graalvm.compiler.asm.sparc.SPARCAssembler.Fcn.Page;
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

    /*@Test
    void shouldShowViewAllTransactionsInPage() throws Exception {
        Wallet wallet = new Wallet(1,0);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(100L, "test 1", TransactionType.CREDIT, wallet));
        PageImpl pageImpl = new PageImpl<Transaction>(transactions, PageRequest.of(0, 10),transactions.size());
        when(walletService.findWalletForUser(anyLong())).thenReturn(wallet);
        when(transactionService.findPaginatedTransactions(PageRequest.of(anyInt(), 10),anyLong())).thenReturn(pageImpl);
        mockMvc.perform(post("/dashboard/transactions").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("transactionPage", isA(Page.getClass())))
                .andExpect(view().name("transactions/show"));
    }*/

    @Test
    void shouldShowFilteredTransactions() throws Exception {
        Wallet wallet = new Wallet(1,0);
        when(walletService.findWalletForUser(anyLong())).thenReturn(wallet);
        mockMvc.perform(post("/dashboard/transactions/filter").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fromDate", "2019-11-12")
                .param("toDate", "2019-11-20"))
                .andExpect(status().isOk())
                .andExpect(view().name("transactions/show"));
    }

}
