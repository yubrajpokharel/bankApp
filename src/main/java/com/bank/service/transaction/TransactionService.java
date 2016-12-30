package com.bank.service.transaction;

import com.bank.domain.*;

import java.security.Principal;
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

    void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount, SavingAccount savingsAccount) throws Exception;

    List<Recipient> findRecipientList(Principal principal);
    Recipient saveRecipient(Recipient recipient);

    Recipient findRecipientByName(String recipientName);
    void deleteRecipientByName(String recipientName);
    void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingAccount savingsAccount);

    boolean checkAmount(String accountType, String amount, String username);

}
