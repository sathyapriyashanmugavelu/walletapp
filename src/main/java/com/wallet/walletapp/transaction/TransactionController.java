package com.wallet.walletapp.transaction;

import com.wallet.walletapp.user.UserService;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/dashboard/transactions")
class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    @RequestMapping("/new")
    String newTransaction(Model model) {
        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        Transaction transaction = new Transaction();
        model.addAttribute("walletId", wallet.getId());
        model.addAttribute("transaction", transaction);
        return "transactions/new";
    }

    @PostMapping(params = "submit")
    String create(@Valid @ModelAttribute Transaction transaction,
                  BindingResult bindingResult) throws WalletNotFoundException {
        if (bindingResult.hasErrors()) {
            return "transactions/new";
        }
        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        transactionService.create(transaction, wallet.getId());
        return "redirect:/dashboard";
    }

    @PostMapping(params = "cancel")
    String cancelNewTransaction(@Valid @ModelAttribute Transaction transaction,
                                BindingResult bindingResult) {
        return "redirect:/dashboard";
    }

    @RequestMapping
    String showTransaction(Model model) {
        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        List<Transaction> transactions = transactionService.findTransaction(wallet.getId());
        model.addAttribute("walletId", wallet.getId());
        model.addAttribute("transaction", transactions);
        return "transactions/show";
    }

    @PostMapping("/filter")
    String showFilteredTransaction(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, Model model) {
        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        List<Transaction> transactions = transactionService.getFilteredTransactions(wallet.getId(), fromDate, toDate);
        model.addAttribute("walletId", wallet.getId());
        model.addAttribute("transaction", transactions);
        return "transactions/show";
    }
}
