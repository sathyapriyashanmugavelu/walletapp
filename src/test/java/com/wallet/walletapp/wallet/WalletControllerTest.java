package com.wallet.walletapp.wallet;

import com.wallet.walletapp.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
@WithMockUser
class WalletControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WalletService walletService;

    @MockBean
    UserService userService;

    @Test
    void shouldGetWalletById() throws Exception {
        when(walletService.fetch(1L)).thenReturn(new Wallet());
        mockMvc.perform(get("/wallets/1"))
                .andExpect(status().isOk());
    }
}