package com.wallet.walletapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GreetingController.class)
class GreetingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldDisplayGreet() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("greetMessage", "Hello World"))
                .andExpect(view().name("home"));
    }

    @Test
    void shouldDisplayGreetWithName() throws Exception {
        mockMvc.perform(get("/?name=test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("greetMessage", "Hello test"))
                .andExpect(view().name("home"));
    }
}
