package com.ajin.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author ajin
 * @create 2023-05-12 22:16
 */
public class JwtToken implements AuthenticationToken {

    private String token;
    public JwtToken(String jwt){
        this.token = jwt;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
