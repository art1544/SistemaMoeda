package com.puc.moeda.controller;

import com.puc.moeda.model.Company;
import com.puc.moeda.model.Professor;
import com.puc.moeda.model.Student;
import com.puc.moeda.model.Transaction;
import com.puc.moeda.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentProfile(@PathVariable Long studentId) {
        try {
            Student student = profileService.getStudentProfile(studentId);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<?> getProfessorProfile(@PathVariable Long professorId) {
        try {
            Professor professor = profileService.getProfessorProfile(professorId);
            return ResponseEntity.ok(professor);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getCompanyProfile(@PathVariable Long companyId) {
        try {
            Company company = profileService.getCompanyProfile(companyId);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/student/{studentId}/transactions")
    public ResponseEntity<?> getStudentTransactionHistory(@PathVariable Long studentId) {
        try {
            List<Transaction> transactions = profileService.getStudentTransactionHistory(studentId);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/professor/{professorId}/transactions")
    public ResponseEntity<?> getProfessorTransactionHistory(@PathVariable Long professorId) {
        try {
            List<Transaction> transactions = profileService.getProfessorTransactionHistory(professorId);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/company/{companyId}/transactions")
    public ResponseEntity<?> getCompanyTransactionHistory(@PathVariable Long companyId) {
        try {
            List<Transaction> transactions = profileService.getCompanyTransactionHistory(companyId);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = profileService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
