package com.puc.moeda.controller;

import com.puc.moeda.dto.RedeemAdvantageDTO;
import com.puc.moeda.dto.TransferRequestDTO;
import com.puc.moeda.model.Transaction;
import com.puc.moeda.service.CoinService;
import com.puc.moeda.exception.ProfessorNotFoundException;
import com.puc.moeda.exception.StudentNotFoundException;
import com.puc.moeda.exception.InsufficientBalanceException;
import com.puc.moeda.exception.AdvantageNotFoundException;
import com.puc.moeda.exception.InvalidRedemptionCodeException;
import com.puc.moeda.exception.RedemptionCodeAlreadyUsedException;
import com.puc.moeda.repository.ProfessorRepository;
import com.puc.moeda.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/transfer/professor/{professorId}")
    public ResponseEntity<?> transferCoinsByProfessor(
            @PathVariable Long professorId,
            @Valid @RequestBody TransferRequestDTO transferRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            coinService.transferCoins(professorId, transferRequest);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Coins transferred successfully");
            return ResponseEntity.ok(successResponse);
        } catch (ProfessorNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (StudentNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (InsufficientBalanceException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/redeem/student/{studentId}")
    public ResponseEntity<?> redeemAdvantageByStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody RedeemAdvantageDTO redeemRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            Transaction redeemedTransaction = coinService.redeemAdvantage(studentId, redeemRequest);
            return ResponseEntity.ok(redeemedTransaction);
        } catch (StudentNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (AdvantageNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (InsufficientBalanceException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/verify-redemption")
    public ResponseEntity<?> verifyRedemptionCode(@RequestBody Map<String, String> requestBody) {
        String redemptionCode = requestBody.get("code");
        if (redemptionCode == null || redemptionCode.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Redemption code is required");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            Transaction verifiedTransaction = coinService.verifyRedemptionCode(redemptionCode);
            return ResponseEntity.ok(verifiedTransaction);
        } catch (InvalidRedemptionCodeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (RedemptionCodeAlreadyUsedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/professor/{professorId}/balance")
    public ResponseEntity<?> updateProfessorBalance(
            @PathVariable Long professorId,
            @RequestBody Map<String, Object> requestBody) {
        
        try {
            Object amountObj = requestBody.get("amount");
            if (amountObj == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Amount is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            BigDecimal amount;
            try {
                if (amountObj instanceof String) {
                    amount = new BigDecimal((String) amountObj);
                } else if (amountObj instanceof Number) {
                    amount = new BigDecimal(amountObj.toString());
                } else {
                    throw new NumberFormatException("Invalid amount format");
                }
            } catch (NumberFormatException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid amount format");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            var professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new ProfessorNotFoundException("Professor not found with id: " + professorId));

            professor.setCoinBalance(amount);
            professorRepository.save(professor);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "Professor balance updated successfully");
            successResponse.put("newBalance", professor.getCoinBalance());
            return ResponseEntity.ok(successResponse);

        } catch (ProfessorNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/student/{studentId}/balance")
    public ResponseEntity<?> updateStudentBalance(
            @PathVariable Long studentId,
            @RequestBody Map<String, Object> requestBody) {
        
        try {
            Object amountObj = requestBody.get("amount");
            if (amountObj == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Amount is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            BigDecimal amount;
            try {
                if (amountObj instanceof String) {
                    amount = new BigDecimal((String) amountObj);
                } else if (amountObj instanceof Number) {
                    amount = new BigDecimal(amountObj.toString());
                } else {
                    throw new NumberFormatException("Invalid amount format");
                }
            } catch (NumberFormatException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid amount format");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            var student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

            student.setCoinBalance(amount);
            studentRepository.save(student);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "Student balance updated successfully");
            successResponse.put("newBalance", student.getCoinBalance());
            return ResponseEntity.ok(successResponse);

        } catch (StudentNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
