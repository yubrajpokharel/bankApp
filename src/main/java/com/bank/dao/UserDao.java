package com.bank.dao;

import com.bank.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yubraj on 12/22/16.
 */

@Service
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    User findByPhone(String phone);
}
