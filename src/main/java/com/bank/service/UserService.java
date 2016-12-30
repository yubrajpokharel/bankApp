package com.bank.service;

import com.bank.domain.User;
import com.bank.domain.security.UserRole;

import java.util.Set;

/**
 * Created by yubraj on 12/22/16.
 */
public interface UserService {
    void save(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    boolean checkUserExists(String username, String email, String phone);
    boolean checkUsernameExists(String username);
    boolean checkEmailExists(String email);
    boolean checkUserPhone(String phone);
    User createUser(User user, Set<UserRole> userRoles);
}
