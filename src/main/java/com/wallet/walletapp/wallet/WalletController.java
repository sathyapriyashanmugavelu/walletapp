package com.wallet.walletapp.wallet;

import java.util.List;

import com.wallet.walletapp.transaction.Transaction;
import com.wallet.walletapp.transaction.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/wallets/{id}")
    String get(Model model, @PathVariable Long id) throws WalletNotFoundException {
        Wallet wallet = walletService.fetch(id);
        model.addAttribute("wallet", wallet);
        List<Transaction> recentTransactions = transactionService.getRecentTransactions(id);
        model.addAttribute("recentTransactions", recentTransactions);
        return "wallets/show";
    }
}
