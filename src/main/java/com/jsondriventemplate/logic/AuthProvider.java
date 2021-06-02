package com.jsondriventemplate.logic;

import com.jsondriventemplate.AppInject;
import com.jsondriventemplate.constant.AuthConst;
import com.jsondriventemplate.exception.AuthenticationException;
import com.jsondriventemplate.repo.DBConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthProvider {

    public static UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken(String username, String password) throws AuthenticationException {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new AuthenticationException(AuthConst.NO_USERNAME_PASSWORD);
        }
        Set<GrantedAuthority> authoritySet = new HashSet<>();
        Map user = AppInject.mongoClientProvider.findByAtt(AuthConst.USERNAME, username, DBConstant.EMPLOYEE);
        if (user == null || user.isEmpty()) {
            throw new UsernameNotFoundException(AuthConst.NO_USERNAME);
        }
        boolean validPassword = AppInject.passwordEncoder.matches(password, (String) user.get(AuthConst.PASSWORD));

        if (!validPassword) {
            throw new UsernameNotFoundException(AuthConst.NO_USERNAME);
        }

        if (user.containsKey(AuthConst.ROLE) && StringUtils.equalsAnyIgnoreCase((String) user.get(AuthConst.ROLE), AuthConst.SUPER_ADMIN_ROLE)) {
            authoritySet.add(new SimpleGrantedAuthority(AuthConst.ROLE_PREFIX + StringUtils.upperCase((String) user.get(AuthConst.ROLE))));
        } else {
            authoritySet.add(new SimpleGrantedAuthority(AuthConst.ROLE_PREFIX + AuthConst.USER_ROLE));
        }
        return new UsernamePasswordAuthenticationToken(username, password, authoritySet);
    }


}
