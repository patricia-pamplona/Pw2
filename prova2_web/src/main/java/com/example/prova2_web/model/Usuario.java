package com.example.prova2_web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Usuario implements UserDetails {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Boolean admin;
    Boolean accountExpired = false;
    Boolean accountLocked = false;
    Boolean credentialsExpired = false;
    Boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Boolean isAdmin() {
        return admin;
    }
}