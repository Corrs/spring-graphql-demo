package com.yanxuan88.australiacallcenter.config;

import com.yanxuan88.australiacallcenter.common.UserLoginInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class AusAuthenticationToken extends AbstractAuthenticationToken {
    private final String principal;
    private UserLoginInfo credentials;

    private AusAuthenticationToken(String principal, UserLoginInfo credentials,
                                   Collection<AusGrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    public static AusAuthenticationToken authenticated(String principal,
                                                       UserLoginInfo credentials,
                                                       Collection<AusGrantedAuthority> authorities) {
        return new AusAuthenticationToken(principal, credentials, authorities);
    }


    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities().stream()
                .flatMap(e -> {
                    AusGrantedAuthority authority = (AusGrantedAuthority) e;
                    return authority.getPermissions().stream();
                })
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
