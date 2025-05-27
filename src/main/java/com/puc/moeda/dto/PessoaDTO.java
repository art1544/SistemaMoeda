package com.puc.moeda.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;
import com.puc.moeda.model.TipoPessoa;
import com.puc.moeda.validation.ValidPassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PessoaDTO {
	
	private Long id;
	
	@CPF(message = "CPF inválido")
	private String cpf;

	@NotBlank(message = "O nome é obrigatório")
	private String nome;
	
	@NotBlank(message = "O email é obrigatório")
	private String email;
	
	@NotBlank(message = "A senha é obrigatória")
	@ValidPassword
	private String senha;
	
	@NotNull(message = "A data de nascimento é obrigatória")
	private LocalDate dataNasc;
	
	@NotNull(message = "O tipo de pessoa é obrigatório")
	private TipoPessoa tipo;
	
	private BigDecimal saldo = BigDecimal.ZERO;
	
	public PessoaDTO() {
	}
	
	public PessoaDTO(Long id, String cpf, String nome, String email, String senha, 
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

	public String toString() {
		return id + " " + nome;
	}
}
