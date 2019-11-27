package com.wallet.walletapp.transaction;

import com.wallet.walletapp.user.UserService;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Controller
@RequestMapping("/wallet/transactions")
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

    @PostMapping
    String create(@Valid @ModelAttribute Transaction transaction,
                  BindingResult bindingResult) throws WalletNotFoundException {
        if(bindingResult.hasErrors()) {
            return "transactions/new";
        }
        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        transactionService.create(transaction, wallet.getId());
        return "redirect:/wallet";
    }

    @RequestMapping("/show")
    String showTransaction(Model model){
        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        List<Transaction> transactions = transactionService.findTransaction(wallet.getId());
        model.addAttribute("walletId", wallet.getId());
        model.addAttribute("transaction", transactions);
        return "transactions/show";
    }
}
