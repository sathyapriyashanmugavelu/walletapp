package com.wallet.walletapp.user;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    WalletService walletService;

    @Test
    void shouldDisplaySignUp() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"));
    }

    @Test
    void shouldDisplayTheErrorMessageWhenUserAlreadyAvailable() throws Exception {
        Wallet wallet = new Wallet(1L, 1000);
        when(walletService.findWalletForUser(anyLong())).thenReturn(Optional.of(wallet).get());
        User user = new User(1, "test", "test123");
        when(userService.findUserByUsername(any())).thenReturn(Optional.of(user));
        mockMvc.perform(post("/users").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "test")
                .param("password", "test123"))
                .andExpect(model().attribute("userAlreadyAvailable", "Username already in use"))
                .andExpect(model().attribute("user", isA(User.class)))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"));
    }

    @Test
    void shouldCreateNewUser() throws Exception {
        User user = new User();
        mockMvc.perform(post("/users").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "test")
                .param("password", "test123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userService).create(argument.capture());
        assertEquals("test", argument.getValue().getUserName());
    }

}
