package com.puc.moeda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransferRequestDTO {

    @NotNull(message = "O ID do aluno não pode ser nulo")
    private Long studentId;

    @NotNull(message = "O valor da transferência não pode ser nulo")
    @Positive(message = "O valor da transferência deve ser positivo")
    private BigDecimal amount;

    @NotBlank(message = "O motivo da transferência não pode estar vazio")
    private String reason;


    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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
}
