package com.example.backend.entity;

import com.example.backend.enums.RoleName;
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

        Hibernate.initialize(user.getUserRoles());
        Set<UserRole> userRoles = user.getUserRoles();
        if (userRoles == null || !Hibernate.isInitialized(userRoles)) {
            throw new RuntimeException("User roles are not available or not initialized for user: " + user.getEmail());
        }

        for (UserRole userRole : new ArrayList<>(userRoles)) {
            Role role = userRole.getRole();
            Hibernate.initialize(role);
            Hibernate.initialize(role.getRolePermission());
            if (role == null) {
                throw new RuntimeException("Role is null in UserRole for user: " + user.getEmail());
            }

            String roleName = role.getRoleName().name();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));

            Set<RolePermission> rolePermissions = role.getRolePermission();
            if (rolePermissions == null || !Hibernate.isInitialized(rolePermissions)) {
                throw new RuntimeException("Role permissions are null or not initialized for role: " + roleName);
            }

            for (RolePermission rolePermission : new ArrayList<>(rolePermissions)) {
                if (rolePermission.getPermission() == null) {
                    throw new RuntimeException("Permission is null in RolePermission for role: " + roleName);
                }

                String permissionName = rolePermission.getPermission().getPermissionName();
                authorities.add(new SimpleGrantedAuthority(permissionName));
            }
        }

        return authorities;
    }

    public String getFullName() {
        return (user.getFirstName() + " " + user.getLastName()).trim();
    }

    public int getId(){
        return user.getId();
    }

    public RoleName getRole() {
        Set<UserRole> userRoles = user.getUserRoles();
        if (userRoles != null && !userRoles.isEmpty()) {
            return userRoles.iterator().next().getRole().getRoleName();
        }
        return RoleName.USER;
    }
    public String getDepartment() {
        return user.getDepartment();
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