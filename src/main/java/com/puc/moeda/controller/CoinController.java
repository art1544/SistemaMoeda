package com.puc.moeda.controller;

import com.puc.moeda.dto.RedeemAdvantageDTO;
import com.puc.moeda.dto.TransferRequestDTO;
import com.puc.moeda.service.CoinService;
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
        } catch (RuntimeException e) {
            // TODO: More specific exception handling (e.g., InsufficientBalanceException, NotFoundException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
            coinService.redeemAdvantage(studentId, redeemRequest);
            return ResponseEntity.ok("Advantage redeemed successfully");
        } catch (RuntimeException e) {
            // TODO: More specific exception handling (e.g., InsufficientBalanceException, NotFoundException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
