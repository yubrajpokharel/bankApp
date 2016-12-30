package com.bank.service.transaction.impl;

import com.bank.dao.accounts.PrimaryAccountDao;
import com.bank.dao.transactions.PrimaryTransactionDao;
import com.bank.dao.transactions.SavingTransactionDao;
import com.bank.domain.PrimaryTransaction;
import com.bank.domain.SavingTransaction;
import com.bank.domain.User;
import com.bank.service.UserService;
import com.bank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private SavingTransactionDao savingTransactionDao;

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
}
