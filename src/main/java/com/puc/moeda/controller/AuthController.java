package com.puc.moeda.controller;

import com.puc.moeda.dto.LoginDTO;
import com.puc.moeda.dto.PasswordRecoveryRequestDTO;
import com.puc.moeda.dto.PasswordResetDTO;
import com.puc.moeda.service.AuthService;
import com.puc.moeda.service.PasswordRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            // Call AuthService to authenticate and get UserDetails
            UserDetails userDetails = authService.authenticateUser(loginDTO);

            // Authentication successful, return basic user info or success message
            // We are no longer returning a JWT
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Authentication successful");
            response.put("username", userDetails.getUsername());
            response.put("roles", userDetails.getAuthorities()); // Return roles if needed on frontend
            // TODO: Include more user-specific info like ID, type (student, professor, company) if needed on frontend

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during authentication");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordRecoveryRequestDTO requestDTO, BindingResult bindingResult) {
         if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            passwordRecoveryService.createPasswordResetToken(requestDTO.getEmail());
            return ResponseEntity.ok("Password recovery email sent");
        } catch (RuntimeException e) {
             // TODO: More specific exception handling
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

     @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDTO resetDTO, BindingResult bindingResult) {
         if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            passwordRecoveryService.resetPassword(resetDTO);
            return ResponseEntity.ok("Password has been reset successfully");
        } catch (RuntimeException e) {
             // TODO: More specific exception handling
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

     private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
