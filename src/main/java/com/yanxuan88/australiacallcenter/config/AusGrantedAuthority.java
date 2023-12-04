package com.yanxuan88.australiacallcenter.config;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class AusGrantedAuthority implements GrantedAuthority {
    private final String role;
    private final List<String> permissions;

    public AusGrantedAuthority(String role, List<String> permissions) {
        this.role = role;
        if (permissions == null) permissions = new ArrayList<>();
        this.permissions = permissions;
    }


    @Override
    public String getAuthority() {
        return role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

}
