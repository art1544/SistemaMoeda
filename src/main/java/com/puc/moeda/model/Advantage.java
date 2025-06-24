package com.puc.moeda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Entity
public class Advantage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da vantagem não pode estar em branco")
    private String name;

    @NotBlank(message = "A descrição da vantagem não pode estar vazia")
    private String description;

    @NotBlank(message = "A URL da imagem não pode estar vazia")
    private String imageUrl;

    @NotNull(message = "O custo em moedas não pode ser nulo")
    @Positive(message = "O custo em moedas deve ser positivo")
    private BigDecimal costInCoins;

    @ManyToOne
    @NotNull(message = "A empresa não pode ser nula")
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
