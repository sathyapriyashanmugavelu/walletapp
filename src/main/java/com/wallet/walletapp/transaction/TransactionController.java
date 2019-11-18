package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.WalletNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallets/{walletId}/transactions")
class TransactionController {
    @Autowired
    TransactionService transactionService;

    @RequestMapping("/new")
    String newTransaction(@PathVariable Long walletId, Model model) {
        Transaction transaction = new Transaction();
        model.addAttribute("walletId", walletId);
        model.addAttribute("transaction", transaction);
        return "transactions/new";
    }

    @PostMapping
    String create(@ModelAttribute Transaction transaction, @PathVariable Long walletId) throws WalletNotFoundException {
        transactionService.create(transaction, walletId);
        return "redirect:/wallets/" + walletId;
    }
}
