package com.wallet.walletapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldDisplayGreet() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void shouldDisplayGreetWithName() throws Exception {
        mockMvc.perform(get("/?name=test&walletId=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("greetMessage", "Hello test"))
                .andExpect(model().attribute("balance", "4000"))
                .andExpect(view().name("home"));
    }
}
