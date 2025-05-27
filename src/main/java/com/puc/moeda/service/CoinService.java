package com.puc.moeda.service;

import com.puc.moeda.dto.RedeemAdvantageDTO;
import com.puc.moeda.dto.TransferRequestDTO;
import com.puc.moeda.model.*;
import com.puc.moeda.repository.AdvantageRepository;
import com.puc.moeda.repository.ProfessorRepository;
import com.puc.moeda.repository.StudentRepository;
import com.puc.moeda.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID; // For generating redemption codes

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
                .orElseThrow(() -> new RuntimeException("Professor not found")); // TODO: Custom exception

        Student student = studentRepository.findById(transferRequest.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found")); // TODO: Custom exception

        BigDecimal amount = transferRequest.getAmount();

        if (professor.getCoinBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance"); // TODO: Custom exception
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
    public void redeemAdvantage(Long studentId, RedeemAdvantageDTO redeemRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found")); // TODO: Custom exception

        Advantage advantage = advantageRepository.findById(redeemRequest.getAdvantageId())
                .orElseThrow(() -> new RuntimeException("Advantage not found")); // TODO: Custom exception

        BigDecimal cost = advantage.getCostInCoins();

        if (student.getCoinBalance().compareTo(cost) < 0) {
            throw new RuntimeException("Insufficient balance"); // TODO: Custom exception
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
        transactionRepository.save(transaction);

        // Generate redemption code
        String redemptionCode = UUID.randomUUID().toString();
        // TODO: Store this redemption code in the transaction or a separate entity for validation by the company

        // Send coupon email to student
        String studentEmailSubject = "Resgate de Vantagem Confirmado: " + advantage.getName();
        String studentEmailBody = String.format(
                "Olá %s, Você resgatou a vantagem %s. Custo: %s moedas. Seu novo saldo é de %s moedas. Apresente o código abaixo na empresa parceira (%s) para utilizar sua vantagem: %s Detalhes da Vantagem: %s",
                student.getName(),
                advantage.getName(),
                cost,
                student.getCoinBalance(),
                advantage.getCompany().getName(),
                redemptionCode,
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
                redemptionCode,
                advantage.getDescription()
        );
        emailService.sendSimpleEmail(advantage.getCompany().getEmail(), companyEmailSubject, companyEmailBody);
    }
}
