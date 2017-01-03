package com.bank.service.account;

import com.bank.domain.PrimaryAccount;
import com.bank.domain.SavingAccount;

/**
 * Created by yubraj on 12/29/16.
 */

public interface AccountService {
    PrimaryAccount createPrimaryAccount();
    SavingAccount createSavingAccount();
    void deposit(String accountType, double amount, String username);
    void withdraw(String accountType, double amount, String username);

}
