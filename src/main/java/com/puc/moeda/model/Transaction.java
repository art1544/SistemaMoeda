package com.puc.moeda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_professor_id")
    private Professor senderProfessor;

    @ManyToOne
    @JoinColumn(name = "sender_student_id")
    private Student senderStudent;

    @ManyToOne
    @JoinColumn(name = "receiver_student_id")
    private Student receiverStudent;

    @ManyToOne
    @JoinColumn(name = "receiver_company_id")
    private Company receiverCompany;

    private BigDecimal amount;
    private String reason;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "advantage_id")
    private Advantage advantage;

    private String redemptionCode;
    private boolean isUsed = false;
    private LocalDateTime usedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Professor getSenderProfessor() {
        return senderProfessor;
    }

    public void setSenderProfessor(Professor senderProfessor) {
        this.senderProfessor = senderProfessor;
    }

    public Student getSenderStudent() {
        return senderStudent;
    }

    public void setSenderStudent(Student senderStudent) {
        this.senderStudent = senderStudent;
    }

    public Student getReceiverStudent() {
        return receiverStudent;
    }

    public void setReceiverStudent(Student receiverStudent) {
        this.receiverStudent = receiverStudent;
    }

    public Company getReceiverCompany() {
        return receiverCompany;
    }

    public void setReceiverCompany(Company receiverCompany) {
        this.receiverCompany = receiverCompany;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Advantage getAdvantage() {
        return advantage;
    }

    public void setAdvantage(Advantage advantage) {
        this.advantage = advantage;
    }

    public String getRedemptionCode() {
        return redemptionCode;
    }

    public void setRedemptionCode(String redemptionCode) {
        this.redemptionCode = redemptionCode;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
}
