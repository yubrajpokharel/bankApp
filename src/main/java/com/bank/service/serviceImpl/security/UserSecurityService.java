package com.bank.service.serviceImpl.security;

import com.bank.dao.UserDao;
import com.bank.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by yubraj on 12/29/16.
 */

@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if(user == null) {
            LOG.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username - " + username + " Not found!");
        }
        return user;

    }
}
