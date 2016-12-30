package com.bank.dao.transactions;

import com.bank.domain.PrimaryTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yubraj on 12/29/16.
 */

public interface PrimaryTransactionDao extends CrudRepository<PrimaryTransaction, Long> {
    List<PrimaryTransaction> findAll();
}
