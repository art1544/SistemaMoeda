package com.puc.moeda.controller;

import com.puc.moeda.dto.CompanyRegistrationDTO;
import com.puc.moeda.dto.ProfessorRegistrationDTO;
import com.puc.moeda.dto.StudentRegistrationDTO;
import com.puc.moeda.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegistrationDTO registrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            registrationService.registerStudent(registrationDTO);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Student registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/professor")
    public ResponseEntity<?> registerProfessor(@Valid @RequestBody ProfessorRegistrationDTO registrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            registrationService.registerProfessor(registrationDTO);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Professor registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/company")
    public ResponseEntity<?> registerCompany(@Valid @RequestBody CompanyRegistrationDTO registrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            registrationService.registerCompany(registrationDTO);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Company registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
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
                // Se já existe um erro para este campo, concatena as mensagens
                errors.put(field, errors.get(field) + "; " + message);
            } else {
                errors.put(field, message);
            }
        }
        
        return errors;
    }
}
