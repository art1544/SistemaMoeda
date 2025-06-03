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

    @Autowired
    private EmailService emailService;

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

        // Deduct from professor
        professor.setCoinBalance(professor.getCoinBalance().subtract(amount));
        professorRepository.save(professor);

        // Add to student
        student.setCoinBalance(student.getCoinBalance().add(amount));
        studentRepository.save(student);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setSenderProfessor(professor);
        transaction.setReceiverStudent(student);
        transaction.setAmount(amount);
        transaction.setReason(transferRequest.getReason());
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Send email notification to student
        String studentEmailSubject = "Você recebeu " + amount + " moedas de mérito!";
        String studentEmailBody = String.format(
                "Olá %s, Você recebeu %s moedas de mérito do professor %s. Motivo: %s Seu novo saldo é de %s moedas.",
                student.getName(),
                amount,
                professor.getName(),
                transferRequest.getReason(),
                student.getCoinBalance());
        emailService.sendSimpleEmail(student.getEmail(), studentEmailSubject, studentEmailBody);
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

        // Deduct from student
        student.setCoinBalance(student.getCoinBalance().subtract(cost));
        studentRepository.save(student);

        // Record transaction (student to company)
        Transaction transaction = new Transaction();
        transaction.setSenderStudent(student);
        transaction.setReceiverCompany(advantage.getCompany());
        transaction.setAmount(cost);
        transaction.setReason("Resgate: " + advantage.getName());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAdvantage(advantage);
        transaction.setRedemptionCode(UUID.randomUUID().toString()); // Generate and save the code
        transaction.setUsed(false); // Mark as not used initially
        // usedAt is null initially
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Send coupon email to student
        String studentEmailSubject = "Resgate de Vantagem Confirmado: " + advantage.getName();
        String studentEmailBody = String.format(
                "Olá %s, Você resgatou a vantagem %s. Custo: %s moedas. Seu novo saldo é de %s moedas. Apresente o código abaixo na empresa parceira (%s) para utilizar sua vantagem: %s Detalhes da Vantagem: %s",
                student.getName(),
                advantage.getName(),
                cost,
                student.getCoinBalance(),
                advantage.getCompany().getName(),
                savedTransaction.getRedemptionCode(), // Use the saved code
                advantage.getDescription()
        );
        emailService.sendSimpleEmail(student.getEmail(), studentEmailSubject, studentEmailBody);

        // Send notification email to partner company
        String companyEmailSubject = "Resgate de Vantagem: " + advantage.getName();
        String companyEmailBody = String.format(
                "Olá %s, Um aluno resgatou a vantagem %s. Aluno: %s (CPF: %s) Código de Resgate: %s Detalhes da Vantagem: %s",
                advantage.getCompany().getName(),
                advantage.getName(),
                student.getName(),
                student.getCpf(), // Assuming CPF is needed for verification
                savedTransaction.getRedemptionCode(), // Use the saved code
                advantage.getDescription()
        );
        emailService.sendSimpleEmail(advantage.getCompany().getEmail(), companyEmailSubject, companyEmailBody);

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

        // TODO: Add check for redemption code expiration if needed (e.g., add expiry date to Transaction)

        // Mark the transaction as used
        transaction.setUsed(true);
        transaction.setUsedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }
}
