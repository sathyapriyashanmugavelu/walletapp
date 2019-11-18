package com.wallet.walletapp.transaction;

import com.wallet.walletapp.wallet.WalletNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
    String create(@Valid @ModelAttribute Transaction transaction,
                  BindingResult bindingResult, @PathVariable Long walletId) throws WalletNotFoundException {
        if(bindingResult.hasErrors()) {
            return "transactions/new";
        }

        transactionService.create(transaction, walletId);
        return "redirect:/wallets/" + walletId;
    }
}
