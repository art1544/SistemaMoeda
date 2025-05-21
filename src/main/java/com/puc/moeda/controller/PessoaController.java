package com.puc.moeda.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puc.moeda.dto.PessoaDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
	
	@PostMapping("")
	public Object create(@RequestBody @Valid PessoaDTO pessoa, 
		 	BindingResult error) {
		return error.getFieldErrors()
		.stream()
		.map(m -> Map.entry(m.getField(), m.getDefaultMessage()))
		.collect(Collectors.toList());

	}
	
	@GetMapping("/{id}")
	public Object read() {
		System.out.println("kakakakakak");
		return null;
	}
	
	@PutMapping("/{id}")
	public Object update() {
		System.out.println("-----------");
		return null;
	}
	
	@DeleteMapping("/{id}")
	public Object delete(Long id) {
		return null;
	}
}
