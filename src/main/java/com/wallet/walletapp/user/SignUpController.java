package com.wallet.walletapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignUpController {
    @Autowired
    UserService userService;

    @RequestMapping("/signup")
    String newSignUp(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "signup";
    }
}