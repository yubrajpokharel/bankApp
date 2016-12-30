package com.bank.controller;

import com.bank.domain.PrimaryAccount;
import com.bank.domain.SavingAccount;
import com.bank.domain.User;
import com.bank.service.UserService;
import com.bank.service.account.AccountService;
import com.bank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yubraj on 12/29/16.
 */

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/primaryAccount")
    public String primaryAccount(Model model){
        PrimaryAccount primaryAccount = userService.findByUsername(getPrincipal())
                                            .getPrimaryAccount();
        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("primaryTransactionList", transactionService.findPrimaryTransactionList(getPrincipal()));
        return "primaryAccount";
    }

    @RequestMapping("/savingsAccount")
    public String savingAccount(Model model){
        SavingAccount savingAccount = userService.findByUsername(getPrincipal())
                                            .getSavingAccount();
        model.addAttribute("savingsAccount", savingAccount);
        model.addAttribute("savingsTransactionList", transactionService.findSavingTransactionList(getPrincipal()) );
        return "savingsAccount";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.GET)
    public String deposit(Model model){
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");
        return "deposit";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public String depositPost(@ModelAttribute("accountType") String accountType,
                              @ModelAttribute("amount") String amount){
        accountService.deposit(accountType, Double.parseDouble(amount), getPrincipal());
        return "redirect:/userFront";

    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public String withdraw(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");
        return "withdraw";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdrawPOST(@ModelAttribute("amount") String amount,
                               @ModelAttribute("accountType") String accountType) {
        accountService.withdraw(accountType, Double.parseDouble(amount), getPrincipal());
        return "redirect:/userFront";
    }


    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }


}
