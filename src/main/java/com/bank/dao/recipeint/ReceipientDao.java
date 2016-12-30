package com.bank.dao.recipeint;

import com.bank.domain.Recipient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yubraj on 12/29/16.
 */
public interface ReceipientDao extends CrudRepository<Recipient, Long> {
    List<Recipient> findAll();
    Recipient findByName(String recipientName);
    Recipient findByEmail(String email);
    void deleteByName(String recipientName);
}
