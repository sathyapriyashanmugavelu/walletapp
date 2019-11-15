package com.wallet.walletapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionRepository transactionRepository;

    @Test
    void shouldCreateTransaction() throws Exception {
        mockMvc.perform(post("/wallets/1/transactions")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "100")
                .param("remarks", "rent"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/wallets/1"));

        verify(transactionRepository).save(any());
    }
}
