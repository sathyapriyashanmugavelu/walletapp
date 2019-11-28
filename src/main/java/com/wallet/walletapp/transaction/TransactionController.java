package com.wallet.walletapp.transaction;

import com.wallet.walletapp.user.UserService;
import com.wallet.walletapp.wallet.InsufficientBalanceException;
import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletNotFoundException;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                  BindingResult bindingResult) throws WalletNotFoundException, InsufficientBalanceException {
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
    public String showAllTransactions(Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        Page<Transaction> transactionPage = transactionService.findPaginatedTransactions(PageRequest.of(currentPage-1, pageSize),wallet.getId());

        model.addAttribute("transactionPage", transactionPage);

        int totalPages = transactionPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "transactions/show";
    }//todo list

    @PostMapping("/filter")
    String showFilteredTransaction(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, Model model) {
        Wallet wallet = walletService.findWalletForUser(userService.getCurrentUserId());
        List<Transaction> transactions = transactionService.getFilteredTransactions(wallet.getId(), fromDate, toDate);
        model.addAttribute("walletId", wallet.getId());
        model.addAttribute("transaction", transactions);
        return "transactions/show";
    }
}
