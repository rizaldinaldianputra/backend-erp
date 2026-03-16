package com.erp.erp.service;

import com.erp.erp.model.User;
import com.erp.erp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Normalize role: strip any existing "ROLE_" prefix then re-add it
        // This prevents double-prefix bug if DB stores "ROLE_ADMIN" instead of "ADMIN"
        String rawRole = user.getRole() != null ? user.getRole().toUpperCase() : "USER";
        String normalizedRole = rawRole.startsWith("ROLE_") ? rawRole : "ROLE_" + rawRole;

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(normalizedRole)))
                .build();
    }
}
