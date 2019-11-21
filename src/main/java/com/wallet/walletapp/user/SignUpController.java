package com.wallet.walletapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class SignUpController {
    @Autowired
    UserService userService;

    @RequestMapping("/new")
    String newSignUp(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "signup";
    }

    @PostMapping
    String create(@ModelAttribute User user) {
        userService.create(user);
        return "redirect:wallets/1";
    }
}