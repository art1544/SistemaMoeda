package com.puc.moeda.controller;

import com.puc.moeda.dto.AdvantageCreationDTO;
import com.puc.moeda.model.Advantage;
import com.puc.moeda.service.AdvantageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/advantages")
public class AdvantageController {

    @Autowired
    private AdvantageService advantageService;

    @GetMapping
    public ResponseEntity<List<Advantage>> getAllAdvantages() {
        List<Advantage> advantages = advantageService.getAllAdvantages();
        return ResponseEntity.ok(advantages);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getAdvantagesByCompany(@PathVariable Long companyId) {
        try {
            List<Advantage> advantages = advantageService.getAdvantagesByCompany(companyId);
            return ResponseEntity.ok(advantages);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> createAdvantage(
            @Valid @RequestBody AdvantageCreationDTO creationDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            Advantage createdAdvantage = advantageService.createAdvantage(creationDTO);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "Advantage created successfully");
            successResponse.put("advantage", createdAdvantage);
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{advantageId}")
    public ResponseEntity<?> deleteAdvantage(@PathVariable Long advantageId) {
        try {
            advantageService.deleteAdvantage(advantageId);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Advantage deleted successfully");
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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
