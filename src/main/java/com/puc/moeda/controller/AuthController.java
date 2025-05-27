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
@RequestMapping("/api/auth") // Example base path
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            String jwt = authService.authenticateUser(loginDTO);
            // Return the generated JWT
            return ResponseEntity.ok(jwt);
        } catch (AuthenticationException e) {
            // Handle authentication failures (e.g., bad credentials)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (RuntimeException e) {
             // Handle other potential runtime exceptions from the service
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
