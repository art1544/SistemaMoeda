package com.puc.moeda.service;

import com.puc.moeda.dto.RedeemAdvantageDTO;
import com.puc.moeda.dto.TransferRequestDTO;
import com.puc.moeda.model.*;
import com.puc.moeda.repository.AdvantageRepository;
import com.puc.moeda.repository.ProfessorRepository;
import com.puc.moeda.repository.StudentRepository;
import com.puc.moeda.repository.TransactionRepository;
import com.puc.moeda.exception.ProfessorNotFoundException;
import com.puc.moeda.exception.StudentNotFoundException;
import com.puc.moeda.exception.InsufficientBalanceException;
import com.puc.moeda.exception.AdvantageNotFoundException;
import com.puc.moeda.exception.InvalidRedemptionCodeException;
import com.puc.moeda.exception.RedemptionCodeAlreadyUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoinService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AdvantageRepository advantageRepository;

    @Transactional
    public void transferCoins(Long professorId, TransferRequestDTO transferRequest) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor not found with id: " + professorId));

        Student student = studentRepository.findById(transferRequest.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + transferRequest.getStudentId()));

        BigDecimal amount = transferRequest.getAmount();

        if (professor.getCoinBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Professor has insufficient balance to transfer " + amount + " coins.");
        }

        professor.setCoinBalance(professor.getCoinBalance().subtract(amount));
        professorRepository.save(professor);

        student.setCoinBalance(student.getCoinBalance().add(amount));
        studentRepository.save(student);

        Transaction transaction = new Transaction();
        transaction.setSenderProfessor(professor);
        transaction.setReceiverStudent(student);
        transaction.setAmount(amount);
        transaction.setReason(transferRequest.getReason());
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

    }

    @Transactional
    public Transaction redeemAdvantage(Long studentId, RedeemAdvantageDTO redeemRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

        Advantage advantage = advantageRepository.findById(redeemRequest.getAdvantageId())
                .orElseThrow(() -> new AdvantageNotFoundException("Advantage not found with id: " + redeemRequest.getAdvantageId()));

        BigDecimal cost = advantage.getCostInCoins();

        if (student.getCoinBalance().compareTo(cost) < 0) {
            throw new InsufficientBalanceException("Student has insufficient balance to redeem advantage: " + advantage.getName());
        }

        student.setCoinBalance(student.getCoinBalance().subtract(cost));
        studentRepository.save(student);

        Transaction transaction = new Transaction();
        transaction.setSenderStudent(student);
        transaction.setReceiverCompany(advantage.getCompany());
        transaction.setAmount(cost);
        transaction.setReason("Resgate: " + advantage.getName());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAdvantage(advantage);
        transaction.setRedemptionCode(UUID.randomUUID().toString());
        transaction.setUsed(false);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return savedTransaction;
    }

    @Transactional
    public Transaction verifyRedemptionCode(String redemptionCode) {
        Optional<Transaction> transactionOptional = transactionRepository.findByRedemptionCode(redemptionCode);

        if (transactionOptional.isEmpty()) {
            throw new InvalidRedemptionCodeException("Invalid redemption code: " + redemptionCode);
        }

        Transaction transaction = transactionOptional.get();

        if (transaction.isUsed()) {
            throw new RedemptionCodeAlreadyUsedException("Redemption code already used: " + redemptionCode);
        }

        transaction.setUsed(true);
        transaction.setUsedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }
}
