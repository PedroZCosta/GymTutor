package com.gymtutor.gymtutor.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FxLoginService {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public FxLoginService(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String email, String senhaRaw) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            return passwordEncoder.matches(senhaRaw, userDetails.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}