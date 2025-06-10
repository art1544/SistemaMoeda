package com.puc.moeda.controller;

import com.puc.moeda.model.Institution;
import com.puc.moeda.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @GetMapping
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        List<Institution> institutions = institutionService.getAllInstitutions();
        return ResponseEntity.ok(institutions);
    }
}
