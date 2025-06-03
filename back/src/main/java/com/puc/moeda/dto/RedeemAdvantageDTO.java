package com.puc.moeda.dto;

import jakarta.validation.constraints.NotNull;

public class RedeemAdvantageDTO {

    @NotNull(message = "O ID da vantagem não pode ser nulo")
    private Long advantageId;

    // Getters and setters
    public Long getAdvantageId() {
        return advantageId;
    }

    public void setAdvantageId(Long advantageId) {
        this.advantageId = advantageId;
    }
}
