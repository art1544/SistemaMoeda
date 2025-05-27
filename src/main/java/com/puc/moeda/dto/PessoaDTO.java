package com.puc.moeda.dto;

import java.time.LocalDate;

import com.puc.moeda.interfaces.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class PessoaDTO {
	
	@NotNull
	private Long id;
	
	@CPF
	private String cpf;

	@NotBlank(message = "Eu consigo alterar")
	private String nome;
	
	private LocalDate dataNasc;
	
	
	
	public PessoaDTO(Long id, String nome, LocalDate dataNasc) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataNasc = dataNasc;
	}



	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(LocalDate dataNasc) {
		this.dataNasc = dataNasc;
	}

	public String toString() {
		return id + " " + nome;
	}
	
	
}
