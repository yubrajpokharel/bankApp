package com.bank.controller;

import com.bank.domain.PrimaryAccount;
import com.bank.domain.Recipient;
import com.bank.domain.SavingAccount;
import com.bank.domain.User;
import com.bank.service.UserService;
import com.bank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

/**
 * Created by yubraj on 12/29/16.
 */

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/betweenAccounts", method = RequestMethod.GET)
    public String betweenAccounts(Model model) {
        model.addAttribute("transferFrom", "");
        model.addAttribute("transferTo", "");
        model.addAttribute("amount", "");
        System.out.println("I am here");
        return "betweenAccounts";
    }

    @RequestMapping(value = "/betweenAccounts", method = RequestMethod.POST)
    public String betweenAccountsPost(
            @ModelAttribute("transferFrom") String transferFrom,
            @ModelAttribute("transferTo") String transferTo,
            @ModelAttribute("amount") String amount,
            Principal principal
    ) throws Exception {
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingAccount savingsAccount = user.getSavingAccount();
        transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, primaryAccount, savingsAccount);
        return "redirect:/userFront";
    }

    @RequestMapping(value = "/recipient", method = RequestMethod.GET)
    public String recipient(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/save", method = RequestMethod.POST)
    public String recipientPost(@ModelAttribute("recipient") Recipient recipient, Principal principal, Model model) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);
        User recipientUser = userService.findByEmail(recipient.getEmail());

        if(null == recipientUser){
            model.addAttribute("emailError", true);
            model.addAttribute("recipientList", recipientList);
            return "recipient";
        }else if(recipientUser.getUsername().equalsIgnoreCase(principal.getName())){
            model.addAttribute("recipientList", recipientList);
            model.addAttribute("yourEmail", true);
            return "recipient";
        }else {
            User user = userService.findByUsername(principal.getName());
            recipient.setName(recipientUser.getFirstName());
            recipient.setPhone(recipientUser.getPhone());
            PrimaryAccount primaryAccount = recipientUser.getPrimaryAccount();
            recipient.setAccountNumber(Integer.toString(primaryAccount.getAccountNumber()));

            recipient.setUser(user);
            transactionService.saveRecipient(recipient);

            return "redirect:/transfer/recipient";
        }
    }

    @RequestMapping(value = "/recipient/edit", method = RequestMethod.GET)
    public String recipientEdit(@RequestParam(value = "recipientName") String recipientName,
                                Model model, Principal principal){
        Recipient recipient = transactionService.findRecipientByName(recipientName);
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/delete", method = RequestMethod.GET)
    @Transactional
    public String recipientDelete(@RequestParam(value = "recipientName") String recipientName,
                                  Model model, Principal principal){
        transactionService.deleteRecipientByName(recipientName);
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();
        model.addAttribute("recipient", recipient);
        model.addAttribute("recipientList", recipientList);


        return "recipient";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.GET)
    public String toSomeoneElse(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);
        model.addAttribute("recipientList", recipientList);
        model.addAttribute("accountType", "");

        return "toSomeoneElse";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.POST)
    public String toSomeoneElsePost(@ModelAttribute("recipientName") String recipientName,
                                    @ModelAttribute("accountType") String accountType,
                                    @ModelAttribute("amount") String amount,
                                    Principal principal,
                                    Model model) {
        User user = userService.findByUsername(principal.getName());
        Recipient recipient = transactionService.findRecipientByName(recipientName);
        if(transactionService.checkAmount(accountType, amount, principal.getName())){
            transactionService.toSomeoneElseTransfer(recipient, accountType, amount, user.getPrimaryAccount(), user.getSavingAccount());
            return "redirect:/userFront";
        }else{
            model.addAttribute("isAmountLess", true);
            List<Recipient> recipientList = transactionService.findRecipientList(principal);
            model.addAttribute("recipientList", recipientList);
            model.addAttribute("recipientName", recipientName);
            model.addAttribute("accountType", accountType);
            return "toSomeoneElse";
        }
    }

}
