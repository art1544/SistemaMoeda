package com.puc.moeda.service;

import com.puc.moeda.dto.PasswordRecoveryRequestDTO;
import com.puc.moeda.dto.PasswordResetDTO;
import com.puc.moeda.model.Company;
import com.puc.moeda.model.PasswordResetToken;
import com.puc.moeda.model.Professor;
import com.puc.moeda.model.Student;
import com.puc.moeda.repository.CompanyRepository;
import com.puc.moeda.repository.PasswordResetTokenRepository;
import com.puc.moeda.repository.ProfessorRepository;
import com.puc.moeda.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordRecoveryService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void createPasswordResetToken(String email) {
        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        Optional<Professor> professorOptional = professorRepository.findByEmail(email);
        Optional<Company> companyOptional = companyRepository.findByEmail(email);

        if (studentOptional.isEmpty() && professorOptional.isEmpty() && companyOptional.isEmpty()) {
            throw new RuntimeException("User with this email not found"); // TODO: Custom exception
        }

        // Delete any existing tokens for this user
        // TODO: Implement methods in token repository to find by user (student, professor, company)
        // For now, we'll rely on the one-to-one relationship and save a new one

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // Token valid for 30 minutes

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);

        if (studentOptional.isPresent()) {
            resetToken.setStudent(studentOptional.get());
        } else if (professorOptional.isPresent()) {
            resetToken.setProfessor(professorOptional.get());
        } else {
            resetToken.setCompany(companyOptional.get());
        }

        passwordResetTokenRepository.save(resetToken);

        // Send email to user with the token link
        String recoveryLink = "YOUR_FRONTEND_RESET_PASSWORD_URL?token=" + token; // TODO: Configure frontend URL
        String emailSubject = "Recuperação de Senha - Moeda de Mérito";
        String emailBody = String.format(
                "Olá, Você solicitou a recuperação de senha para a sua conta na Moeda de Mérito. Use o link abaixo para redefinir sua senha: %s Este link expirará em 30 minutos. Se você não solicitou esta recuperação, por favor ignore este email.",
                recoveryLink
        );
        emailService.sendSimpleEmail(email, emailSubject, emailBody);
    }

    @Transactional
    public void resetPassword(PasswordResetDTO resetDTO) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(resetDTO.getToken());

        if (tokenOptional.isEmpty() || tokenOptional.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired token"); // TODO: Custom exception
        }

        PasswordResetToken resetToken = tokenOptional.get();

        // TODO: Validate new password and confirm password using a validator
        if (!resetDTO.getNewPassword().equals(resetDTO.getConfirmNewPassword())) {
            throw new RuntimeException("New passwords do not match"); // TODO: Custom exception
        }
        // TODO: Add password complexity validation for new password

        // Find the user associated with the token
        Student student = resetToken.getStudent();
        Professor professor = resetToken.getProfessor();
        Company company = resetToken.getCompany();

        if (student != null) {
            student.setPassword(passwordEncoder.encode(resetDTO.getNewPassword()));
            studentRepository.save(student);
        } else if (professor != null) {
            professor.setPassword(passwordEncoder.encode(resetDTO.getNewPassword()));
            professorRepository.save(professor);
        } else if (company != null) {
            company.setPassword(passwordEncoder.encode(resetDTO.getNewPassword()));
            companyRepository.save(company);
        } else {
             throw new RuntimeException("User not found for this token"); // Should not happen if token is valid
        }

        // Delete the token after successful reset
        passwordResetTokenRepository.delete(resetToken);
    }

    // TODO: Add a scheduled task to clean up expired tokens from the repository
}
