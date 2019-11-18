package com.wallet.walletapp.transaction;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionRepository transactionRepository;

    @Test
    void shouldShowNewTransaction() throws Exception {
        mockMvc.perform(post("/wallets/1/transactions/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("transaction", isA(Transaction.class)))
                .andExpect(view().name("transactions/new"));
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        mockMvc.perform(post("/wallets/1/transactions")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "100")
                .param("remarks", "rent"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/wallets/1"));

        ArgumentCaptor<Transaction> argument = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(argument.capture());
        assertEquals("rent", argument.getValue().getRemarks());
        assertEquals(100L, argument.getValue().getAmount());
    }
}
