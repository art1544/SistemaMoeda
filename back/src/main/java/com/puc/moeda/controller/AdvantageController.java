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

    @PostMapping
    public ResponseEntity<?> createAdvantage(
            @Valid @RequestBody AdvantageCreationDTO creationDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            Advantage createdAdvantage = advantageService.createAdvantage(creationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAdvantage);
        } catch (RuntimeException e) {
            // TODO: More specific exception handling (e.g., CompanyNotFoundException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

     private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
