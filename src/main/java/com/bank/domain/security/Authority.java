package com.bank.domain.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by yubraj on 12/29/16.
 */
public class Authority implements GrantedAuthority{

    private final String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
