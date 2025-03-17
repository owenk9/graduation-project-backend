package com.example.backend.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
@Data
public class CustomUserDetails implements UserDetails {
    private final Users user;

    public CustomUserDetails(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        System.out.println("Getting authorities for user: " + user.getEmail());
        if (user.getRole() != null) {
            String roleName = user.getRole().getRoleName();
            System.out.println("Role: " + roleName);
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            Set<RolePermission> rolePermissions = user.getRole().getRolePermission();
            if (rolePermissions != null) {
                for (RolePermission rolePermission : rolePermissions) {
                    String permissionName = rolePermission.getPermission().getPermissionName();
                    System.out.println("Permission: " + permissionName);
                    authorities.add(new SimpleGrantedAuthority(permissionName));
                }
            } else {
                System.out.println("Role permissions are null");
            }
        } else {
            System.out.println("Role is null");
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
