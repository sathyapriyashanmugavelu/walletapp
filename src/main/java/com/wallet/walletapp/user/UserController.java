package com.wallet.walletapp.user;

import com.wallet.walletapp.transaction.Transaction;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    @RequestMapping("/signup")
    String newUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/signup";
    }

    @PostMapping("/user/new")
    String create(@ModelAttribute User user, Model model) {
        Optional<User> userExists= userService.findUserByUsername(user.getUserName());
        if(userExists.isPresent()){
            model.addAttribute("userAlreadyAvailable","Username already in use");
            model.addAttribute("user",new User());
            return "user/signup";
        }
        userService.create(user);
        //Wallet wallet=walletService.findWalletForUser(user.getId());
        return "redirect:/login";
        //return "redirect:/wallets/"+wallet.getId();
    }
}