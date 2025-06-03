package com.puc.moeda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class AdvantageCreationDTO {

    @NotBlank(message = "O nome da vantagem não pode estar vazio")
    private String name;

    @NotBlank(message = "A descrição da vantagem não pode estar vazia")
    private String description;

    // Assuming image is handled by a URL for now
    @NotBlank(message = "A URL da imagem não pode estar vazia")
    private String imageUrl;

    @NotNull(message = "O custo em moedas não pode ser nulo")
    @Positive(message = "O custo em moedas deve ser positivo")
    private BigDecimal costInCoins;

    @NotNull(message = "O ID da empresa não pode ser nulo")
    private Long companyId;

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getCostInCoins() {
        return costInCoins;
    }

    public void setCostInCoins(BigDecimal costInCoins) {
        this.costInCoins = costInCoins;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
