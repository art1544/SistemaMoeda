package com.puc.moeda.service;

import com.puc.moeda.model.Company;
import com.puc.moeda.model.Professor;
import com.puc.moeda.model.Student;
import com.puc.moeda.model.Transaction;
import com.puc.moeda.repository.CompanyRepository;
import com.puc.moeda.repository.ProfessorRepository;
import com.puc.moeda.repository.StudentRepository;
import com.puc.moeda.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TransactionRepository transactionRepository;    

    public Student getStudentProfile(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Professor getProfessorProfile(Long professorId) {
        return professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));
    }

    public Company getCompanyProfile(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public List<Transaction> getStudentTransactionHistory(Long studentId) {
        Student student = getStudentProfile(studentId);
        return transactionRepository.findBySenderStudentOrReceiverStudent(student, student);
    }

    public List<Transaction> getProfessorTransactionHistory(Long professorId) {
        Professor professor = getProfessorProfile(professorId);
        return transactionRepository.findBySenderProfessor(professor);
    }

    public List<Transaction> getCompanyTransactionHistory(Long companyId) {
        Company company = getCompanyProfile(companyId);
        return transactionRepository.findByReceiverCompany(company);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
