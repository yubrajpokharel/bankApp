package com.bank.dao.accounts;

import com.bank.domain.PrimaryAccount;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yubraj on 12/29/16.
 */
public interface PrimaryAccountDao extends CrudRepository<PrimaryAccount, Long> {
    PrimaryAccount findByAccountNumber(int accountNumber);
}
