package com.wallet.walletapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/wallets/{walletId}/transactions")
class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping("/new")
    String newTransaction(@PathVariable Long walletId, Model model) {
        Transaction transaction = new Transaction();
        model.addAttribute("walletId", walletId);
        model.addAttribute("transaction", transaction);
        return "transactions/new";
    }

    @PostMapping
    String create(@ModelAttribute Transaction transaction, @PathVariable Long walletId) {
        transactionRepository.save(transaction);
        return "redirect:/wallets/" + walletId;
    }
}
