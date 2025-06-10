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
            return ResponseEntity.status(HttpStatus.CREATED).body("Student registered successfully");
        } catch (RuntimeException e) {
            // TODO: More specific exception handling
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/professor")
    public ResponseEntity<?> registerProfessor(@Valid @RequestBody ProfessorRegistrationDTO registrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            registrationService.registerProfessor(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Professor registered successfully");
        } catch (RuntimeException e) {
            // TODO: More specific exception handling
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/company")
    public ResponseEntity<?> registerCompany(@Valid @RequestBody CompanyRegistrationDTO registrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            registrationService.registerCompany(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Company registered successfully");
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
