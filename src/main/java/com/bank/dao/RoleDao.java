package com.bank.dao;

import com.bank.domain.security.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yubraj on 12/29/16.
 */


public interface RoleDao extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
