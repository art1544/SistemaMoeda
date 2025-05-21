package com.puc.moeda.dto;

import java.time.LocalDate;

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



	public String toString() {
		return id + " " + nome;
	}
	
	
}
