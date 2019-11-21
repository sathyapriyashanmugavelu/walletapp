package com.wallet.walletapp;

import com.wallet.walletapp.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class HomeController {
    @RequestMapping("/")
    String greet(Model model, @RequestParam(defaultValue = "World") String name) {
        model.addAttribute("greetMessage", "Hello " + name);
        return "home";
    }
}