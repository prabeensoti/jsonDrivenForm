package com.jsondriventemplate.config;

import com.jsondriventemplate.logic.AuthProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
 
    @Override
    public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        return AuthProvider.usernamePasswordAuthenticationToken(name,password);
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}