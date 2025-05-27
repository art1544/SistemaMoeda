package com.puc.moeda.repository;

import com.puc.moeda.model.Company;
import com.puc.moeda.model.Professor;
import com.puc.moeda.model.Student;
import com.puc.moeda.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderStudentOrReceiverStudent(Student senderStudent, Student receiverStudent);
    List<Transaction> findBySenderProfessor(Professor senderProfessor);
    List<Transaction> findByReceiverCompany(Company receiverCompany);
}
