package com.wallet.walletapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class GreetingController {
    @RequestMapping("/")
    String greet() {
        return "home";
    }
}
