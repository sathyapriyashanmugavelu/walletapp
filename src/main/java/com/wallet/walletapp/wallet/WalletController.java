package com.wallet.walletapp.wallet;

import com.wallet.walletapp.transaction.Transaction;
import com.wallet.walletapp.transaction.TransactionService;
import com.wallet.walletapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/dashboard")
    String get(Model model) throws WalletNotFoundException {
        String username = userService.getUsernameForCurrentUser();
        model.addAttribute("greetMessage", "Hello " + username);
        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        model.addAttribute("wallet", wallet);
        List<Transaction> recentTransactions = transactionService.getRecentTransactions(wallet.getId());
        model.addAttribute("recentTransactions", recentTransactions);
        return "wallets/show";
    }
}
