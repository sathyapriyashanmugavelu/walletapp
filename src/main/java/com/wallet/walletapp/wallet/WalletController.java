package com.wallet.walletapp.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WalletController {
    @Autowired
    private WalletRepository walletRepository;

    @RequestMapping("/wallets/{id}")
    String get(Model model, @PathVariable Long id) {
        Wallet wallet = walletRepository.findById(id).get();
        model.addAttribute("wallet", wallet);
        return "wallets/show";
    }
}
