package com.wallet.walletapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallets/{walletId}/transactions")
class TransactionController {
    @RequestMapping("/new")
    String newTransaction(@PathVariable Long walletId, Model model) {
        Transaction transaction = new Transaction();
        model.addAttribute("walletId", walletId);
        model.addAttribute("transaction", transaction);
        return "transactions/new";
    }
}
