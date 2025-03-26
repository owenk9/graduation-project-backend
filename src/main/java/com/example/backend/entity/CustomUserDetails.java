package com.example.backend.entity;

import lombok.Data;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
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

        Set<UserRole> userRoles = user.getUserRoles();
        if (userRoles != null && Hibernate.isInitialized(userRoles)) {
            for (UserRole userRole : new ArrayList<>(userRoles)) { // Sao chép để tránh lỗi
                Role role = userRole.getRole();
                if (role != null) {
                    String roleName = role.getRoleName();
                    System.out.println("Role: " + roleName);
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));

                    Set<RolePermission> rolePermissions = role.getRolePermission();
                    if (rolePermissions != null && Hibernate.isInitialized(rolePermissions)) {
                        for (RolePermission rolePermission : new ArrayList<>(rolePermissions)) {
                            if (rolePermission.getPermission() != null) {
                                String permissionName = rolePermission.getPermission().getPermissionName();
                                System.out.println("Permission: " + permissionName);
                                authorities.add(new SimpleGrantedAuthority(permissionName));
                            }
                        }
                    } else {
                        System.out.println("Role permissions are null for role: " + roleName);
                    }
                } else {
                    System.out.println("Role is null in UserRole");
                }
            }
        } else {
            System.out.println("User roles are null");
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}