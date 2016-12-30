package com.bank.service.transaction.impl;

import com.bank.dao.accounts.PrimaryAccountDao;
import com.bank.dao.accounts.SavingAccountDao;
import com.bank.dao.recipeint.ReceipientDao;
import com.bank.dao.transactions.PrimaryTransactionDao;
import com.bank.dao.transactions.SavingTransactionDao;
import com.bank.domain.*;
import com.bank.service.UserService;
import com.bank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yubraj on 12/29/16.
 */

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private PrimaryTransactionDao primaryTransactionDao;

    @Autowired
    private PrimaryAccountDao primaryAccountDao;

    @Autowired
    private SavingTransactionDao savingTransactionDao;

    @Autowired
    private SavingAccountDao savingAccountDao;

    @Autowired
    private ReceipientDao recipientDao;

    @Override
    public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

    @Override
    public void saveSavingsDepositTransaction(SavingTransaction savingsTransaction) {
        savingTransactionDao.save(savingsTransaction);
    }

    @Override
    public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

    @Override
    public void saveSavingsWithdrawTransaction(SavingTransaction savingsTransaction) {
        savingTransactionDao.save(savingsTransaction);
    }

    @Override
    public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
        User user = userService.findByUsername(username);
        List<PrimaryTransaction> primaryTransactions = user.getPrimaryAccount().getPrimaryTransactionList();
        return primaryTransactions;
    }

    @Override
    public List<SavingTransaction> findSavingTransactionList(String username) {
        User user = userService.findByUsername(username);
        List<SavingTransaction> savingTransactions = user.getSavingAccount().getSavingTransactionList();
        return savingTransactions;
    }

    @Override
    public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount, SavingAccount savingsAccount) throws Exception {
        if(transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Saving")){
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingAccountDao.save(savingsAccount);
            Date date = new Date();
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Account", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);

        }else if(transferFrom.equalsIgnoreCase("Saving") && transferTo.equalsIgnoreCase("Primary")){
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingAccountDao.save(savingsAccount);
            Date date = new Date();
            SavingTransaction savingsTransaction = new SavingTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingTransactionDao.save(savingsTransaction);

        }else {
            throw new Exception("Some issue is transferring amount");
        }
    }

    @Override
    public List<Recipient> findRecipientList(Principal principal) {
        String username = principal.getName();
        List<Recipient> recipientList = recipientDao.findAll().stream() 			//convert list to stream
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))	//filters the line, equals to username
                .collect(Collectors.toList());

        return recipientList;
    }

    @Override
    public Recipient saveRecipient(Recipient recipient) {
        return recipientDao.save(recipient);
    }

    @Override
    public Recipient findRecipientByName(String recipientName) {
        return recipientDao.findByName(recipientName);
    }

    @Override
    public void deleteRecipientByName(String recipientName) {
        recipientDao.deleteByName(recipientName);
    }

    @Override
    public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingAccount savingsAccount) {


        PrimaryAccount recipientPrimaryAccount = userService.findByEmail(recipient.getEmail()).getPrimaryAccount();

        if (accountType.equalsIgnoreCase("Primary")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            recipientPrimaryAccount.setAccountBalance(recipientPrimaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            primaryAccountDao.save(recipientPrimaryAccount);
            Date date = new Date();
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            PrimaryTransaction recipeintPrimaryTransaction = new PrimaryTransaction(date, "Amount Transferred by "+recipient.getUser().getFirstName(), "Transfer", "Finished", Double.parseDouble(amount), recipientPrimaryAccount.getAccountBalance(), recipientPrimaryAccount);
            primaryTransactionDao.save(primaryTransaction);
            primaryTransactionDao.save(recipeintPrimaryTransaction);

        } else if (accountType.equalsIgnoreCase("Saving")) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            recipientPrimaryAccount.setAccountBalance(recipientPrimaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingAccountDao.save(savingsAccount);
            primaryAccountDao.save(recipientPrimaryAccount);
            Date date = new Date();
            SavingTransaction savingsTransaction = new SavingTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            PrimaryTransaction recipeintPrimaryTransaction = new PrimaryTransaction(date, "Amount Transferred by "+recipient.getUser().getFirstName(), "Transfer", "Finished", Double.parseDouble(amount), recipientPrimaryAccount.getAccountBalance(), recipientPrimaryAccount);
            savingTransactionDao.save(savingsTransaction);
            primaryTransactionDao.save(recipeintPrimaryTransaction);
        }
    }

    public boolean checkAmount(String accountType, String amount, String username){
        System.out.println("Account Type :"+ accountType + " Amount : "+amount+" UserName :"+username);
        User user = userService.findByUsername(username);
        if(accountType.equalsIgnoreCase("primary")){
            if(user.getPrimaryAccount().getAccountBalance().compareTo(new BigDecimal(amount)) < 0 ) {
                System.out.println("Primary amount is less then trasfer amount");
                return false;
            }
        }else if(accountType.equalsIgnoreCase("Saving")){
            if(user.getSavingAccount().getAccountBalance().compareTo(new BigDecimal(amount)) < 0 ) {
                System.out.println("Primary amount is less then saving amount");
                return false;
            }
        }
        return true;
    }

}
