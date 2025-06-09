// src/main/java/com/example/backend/entity/CustomUserDetails.java
package com.example.backend.entity;

import com.example.backend.enums.RoleName;
import lombok.Data;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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
        List<UserRole> userRoles = user.getUserRoles();
        if (userRoles == null || !Hibernate.isInitialized(userRoles)) {
            throw new RuntimeException("User roles are not available or not initialized for user: " + user.getEmail());
        }

        for (UserRole userRole : new ArrayList<>(userRoles)) {
            Role role = userRole.getRole();
            Hibernate.initialize(role);
            if (role == null) {
                throw new RuntimeException("Role is null in UserRole for user: " + user.getEmail());
            }

            String roleName = role.getRoleName().name();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        }
        System.out.println("Authorities for user------------------------------------ " + user.getEmail() + ": " + authorities);
        return authorities;
    }

    public String getFullName() {
        return (user.getFirstName() + " " + user.getLastName()).trim();
    }

    public int getId() {
        return user.getId();
    }

    public RoleName getRole() {
        List<UserRole> userRoles = user.getUserRoles();
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