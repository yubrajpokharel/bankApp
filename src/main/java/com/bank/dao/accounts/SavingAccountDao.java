package com.bank.dao.accounts;

import com.bank.domain.SavingAccount;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yubraj on 12/29/16.
 */
public interface SavingAccountDao extends CrudRepository<SavingAccount, Long> {
    SavingAccount findByAccountNumber(int accountNumber);
}
