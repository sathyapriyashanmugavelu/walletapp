package com.wallet.walletapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WalletController {
    private Map<Integer, Wallet> walletRepository = new HashMap<>();

    public WalletController() {
        walletRepository.put(1, new Wallet("Default", 4000));
        walletRepository.put(2, new Wallet("Default", 3000));
    }

    @RequestMapping("/wallets/{id}")
    String greet(Model model, @PathVariable Integer id) {
        Wallet wallet = walletRepository.get(id);
        model.addAttribute("balance", wallet.balance());
        return "wallets/show";
    }
}
