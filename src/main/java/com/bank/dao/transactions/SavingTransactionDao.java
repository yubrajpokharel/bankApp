package com.bank.dao.transactions;

import com.bank.domain.SavingTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yubraj on 12/29/16.
 */
public interface SavingTransactionDao extends CrudRepository<SavingTransaction, Long> {
    List<SavingTransaction> findAll();
}
