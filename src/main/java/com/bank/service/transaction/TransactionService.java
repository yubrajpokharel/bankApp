package com.bank.service.transaction;

import com.bank.domain.PrimaryTransaction;
import com.bank.domain.SavingTransaction;

import java.util.List;

/**
 * Created by yubraj on 12/29/16.
 */
public interface TransactionService {

    void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);
    void saveSavingsDepositTransaction(SavingTransaction savingsTransaction);

    void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
    void saveSavingsWithdrawTransaction(SavingTransaction savingsTransaction);

    List<PrimaryTransaction> findPrimaryTransactionList(String username);
    List<SavingTransaction> findSavingTransactionList(String username);


}
