package com.bank.service.account.Impl;

import com.bank.dao.accounts.PrimaryAccountDao;
import com.bank.dao.accounts.SavingAccountDao;
import com.bank.domain.*;
import com.bank.service.UserService;
import com.bank.service.account.AccountService;
import com.bank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yubraj on 12/29/16.
 */

@Service
public class AccountServiceImpl implements AccountService {

    public static int NEXT_ACCOUNT_NUMBER = 78263548;

    @Autowired
    private PrimaryAccountDao primaryAccountDao;

    @Autowired
    private SavingAccountDao savingAccountDao;

    @Autowired
    private UserService userService;

    @Autowired
    TransactionService transactionService;

    @Override
    public PrimaryAccount createPrimaryAccount() {
        PrimaryAccount primaryAccount = new PrimaryAccount();
        primaryAccount.setAccountBalance(new BigDecimal(0.0));
        primaryAccount.setAccountNumber(autoGen());
        primaryAccountDao.save(primaryAccount);
        return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
    }

    @Override
    public SavingAccount createSavingAccount() {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setAccountBalance(new BigDecimal(0.0));
        savingAccount.setAccountNumber(autoGen());
        savingAccountDao.save(savingAccount);
        return savingAccountDao.findByAccountNumber(savingAccount.getAccountNumber());
    }

    @Override
    public void deposit(String accountType, double amount, String username) {
        User user = userService.findByUsername(username);
        Date date = new Date();

        if(accountType.equalsIgnoreCase("Primary")){
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);

        }else if(accountType.equalsIgnoreCase("Saving")) {
            SavingAccount savingAccount = user.getSavingAccount();
            savingAccount.setAccountBalance(savingAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingAccountDao.save(savingAccount);

            SavingTransaction savingsTransaction = new SavingTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingAccount.getAccountBalance(), savingAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }

    }

    @Override
    public void withdraw(String accountType, double amount, String username) {
        User user = userService.findByUsername(username);
        Date date = new Date();

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
        } else if (accountType.equalsIgnoreCase("Saving")) {
            SavingAccount savingsAccount = user.getSavingAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingAccountDao.save(savingsAccount);

            SavingTransaction savingsTransaction = new SavingTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
        }
    }

    private int autoGen(){
        return ++NEXT_ACCOUNT_NUMBER;
    }


}
