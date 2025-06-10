package com.puc.moeda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.puc.moeda.validation.CPF;

@Entity
@Table(name = "pessoas")
public class Pessoa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O CPF é obrigatório")
    @CPF
    private String cpf;
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "O email é obrigatório")
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    private String senha;
    
    @NotNull(message = "A data de nascimento é obrigatória")
    private LocalDate dataNasc;
    
    @NotNull(message = "O tipo de pessoa é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipo;
    
    private BigDecimal saldo = BigDecimal.ZERO;
    
    // Construtor padrão
    public Pessoa() {
    }
    
    // Construtor com todos os campos
    public Pessoa(Long id, String cpf, String nome, String email, String senha, 
                 LocalDate dataNasc, TipoPessoa tipo) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNasc = dataNasc;
        this.tipo = tipo;
        this.saldo = BigDecimal.ZERO;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public LocalDate getDataNasc() {
        return dataNasc;
    }
    
    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }
    
    public TipoPessoa getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoPessoa tipo) {
        this.tipo = tipo;
    }
    
    public BigDecimal getSaldo() {
        return saldo;
    }
    
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
} 