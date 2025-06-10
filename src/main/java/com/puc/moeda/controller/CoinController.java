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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

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
            return ResponseEntity.ok("Coins transferred successfully");
        } catch (ProfessorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
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
             // Return details of the redeemed transaction, including the code
            return ResponseEntity.ok(redeemedTransaction);
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AdvantageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/verify-redemption")
    public ResponseEntity<?> verifyRedemptionCode(@RequestBody Map<String, String> requestBody) {
        String redemptionCode = requestBody.get("code");
        if (redemptionCode == null || redemptionCode.trim().isEmpty()) {
            return new ResponseEntity<>("Redemption code is required", HttpStatus.BAD_REQUEST);
        }

        try {
            Transaction verifiedTransaction = coinService.verifyRedemptionCode(redemptionCode);
            // Return details of the verified transaction
            return ResponseEntity.ok(verifiedTransaction);
        } catch (InvalidRedemptionCodeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RedemptionCodeAlreadyUsedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
