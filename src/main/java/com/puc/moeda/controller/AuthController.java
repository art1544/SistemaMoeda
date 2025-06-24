package com.puc.moeda.controller;

import com.puc.moeda.dto.LoginDTO;
import com.puc.moeda.dto.PasswordRecoveryRequestDTO;
import com.puc.moeda.dto.PasswordResetDTO;
import com.puc.moeda.service.AuthService;
import com.puc.moeda.service.PasswordRecoveryService;
import com.puc.moeda.repository.StudentRepository;
import com.puc.moeda.repository.ProfessorRepository;
import com.puc.moeda.repository.CompanyRepository;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            UserDetails userDetails = authService.authenticateUser(loginDTO);

            Map<String, Object> userInfo = getUserInfo(userDetails.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Authentication successful");
            response.put("username", userDetails.getUsername());
            response.put("roles", userDetails.getAuthorities());
            response.put("id", userInfo.get("id"));
            response.put("userType", userInfo.get("userType"));
            response.put("name", userInfo.get("name"));

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred during authentication");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private Map<String, Object> getUserInfo(String email) {
        Map<String, Object> userInfo = new HashMap<>();
        
        studentRepository.findByEmail(email).ifPresent(student -> {
            userInfo.put("id", student.getId());
            userInfo.put("userType", "STUDENT");
            userInfo.put("name", student.getName());
        });
        
        professorRepository.findByEmail(email).ifPresent(professor -> {
            userInfo.put("id", professor.getId());
            userInfo.put("userType", "PROFESSOR");
            userInfo.put("name", professor.getName());
        });
        
        companyRepository.findByEmail(email).ifPresent(company -> {
            userInfo.put("id", company.getId());
            userInfo.put("userType", "COMPANY");
            userInfo.put("name", company.getName());
        });
        
        return userInfo;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordRecoveryRequestDTO requestDTO, BindingResult bindingResult) {
         if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            passwordRecoveryService.createPasswordResetToken(requestDTO.getEmail());
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Password recovery email sent");
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

     @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDTO resetDTO, BindingResult bindingResult) {
         if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            passwordRecoveryService.resetPassword(resetDTO);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Password has been reset successfully");
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

     private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            
            if (errors.containsKey(field)) {
                errors.put(field, errors.get(field) + "; " + message);
            } else {
                errors.put(field, message);
            }
        }
        
        return errors;
    }
}
