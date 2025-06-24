package com.puc.moeda.service;

import com.puc.moeda.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public UserDetails authenticateUser(LoginDTO loginDTO) {

        return userDetailsService.loadUserByUsername(loginDTO.getUsernameOrEmail());
    }
}
