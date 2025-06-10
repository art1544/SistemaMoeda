package com.puc.moeda.service;

import com.puc.moeda.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Removido: @Autowired private JwtUtil jwtUtil;

    public UserDetails authenticateUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsernameOrEmail(),
                        loginDTO.getPassword()
                )
        );

        // Authentication successful, return UserDetails
        // We no longer generate JWT
        return userDetailsService.loadUserByUsername(loginDTO.getUsernameOrEmail());
    }
}
