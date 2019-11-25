package com.wallet.walletapp.user;

import com.wallet.walletapp.transaction.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void shouldDisplaySignUp() throws Exception {
        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"));
    }
    @Test
    void shouldCreateNewUser() throws Exception{
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName","test-user-1")
                .param("password", "foobar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userService).create(argument.capture());
        assertEquals("userName", argument.getValue().getUserName());
    }
}
