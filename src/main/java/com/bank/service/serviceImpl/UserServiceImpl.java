package com.bank.service.serviceImpl;

import com.bank.dao.RoleDao;
import com.bank.dao.UserDao;
import com.bank.domain.User;
import com.bank.domain.security.UserRole;
import com.bank.service.UserService;
import com.bank.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * Created by yubraj on 12/22/16.
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AccountService accountService;

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean checkUserExists(String username, String email, String phone) {
        if(checkUsernameExists(username) || checkEmailExists(email) || checkUserPhone(phone))
            return true;
        return false;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        if(null != findByUsername(username))
            return true;
        return false;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if(null != findByEmail(email))
            return true;
        return false;
    }

    @Override
    public User findByPhone(String phone){
        return userDao.findByPhone(phone);
    }

    @Override
    public boolean checkUserPhone(String phone) {
        if(null != userDao.findByPhone(phone))
            return true;
        return false;
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userDao.findByUsername(user.getUsername());
        if(localUser != null)
            LOG.warn("User with username {} already exists", user.getUsername());
        else{
            user.setPassword(encoder.encode(user.getPassword()));

            for (UserRole ur : userRoles) {
                roleDao.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);
            user.setPrimaryAccount(accountService.createPrimaryAccount());
            user.setSavingAccount(accountService.createSavingAccount());
            localUser = userDao.save(user);
        }
        return localUser;
    }

    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }

    @Override
    public List<User> findUserList() {
        return userDao.findAll();
    }

    @Override
    public void enableUser(String username) {
        User user = userDao.findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
    }

    @Override
    public void disableUser(String username) {
        User user = userDao.findByUsername(username);
        user.setEnabled(false);
        userDao.save(user);
    }
}