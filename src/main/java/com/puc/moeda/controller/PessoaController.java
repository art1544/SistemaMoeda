package com.puc.moeda.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puc.moeda.dto.LoginDTO;
import com.puc.moeda.dto.PessoaDTO;
import com.puc.moeda.model.Pessoa;
import com.puc.moeda.repository.PessoaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@PostMapping
	public ResponseEntity<PessoaDTO> criar(@Valid @RequestBody PessoaDTO pessoaDTO) {
		if (pessoaRepository.existsByCpf(pessoaDTO.getCpf())) {
			return ResponseEntity.badRequest().build();
		}
		if (pessoaRepository.existsByEmail(pessoaDTO.getEmail())) {
			return ResponseEntity.badRequest().build();
		}

		Pessoa pessoa = new Pessoa();
		pessoa.setCpf(pessoaDTO.getCpf());
		pessoa.setNome(pessoaDTO.getNome());
		pessoa.setEmail(pessoaDTO.getEmail());
		pessoa.setSenha(pessoaDTO.getSenha());
		pessoa.setDataNasc(pessoaDTO.getDataNasc());
		pessoa.setTipo(pessoaDTO.getTipo());
		pessoa.setSaldo(pessoaDTO.getSaldo());

		pessoa = pessoaRepository.save(pessoa);
		pessoaDTO.setId(pessoa.getId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<PessoaDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
		return pessoaRepository.findByEmail(loginDTO.getEmail())
			.filter(pessoa -> pessoa.getSenha().equals(loginDTO.getSenha()))
			.map(pessoa -> ResponseEntity.ok(converterParaDTO(pessoa)))
			.orElse(ResponseEntity.badRequest().build());
	}

	@GetMapping
	public ResponseEntity<List<PessoaDTO>> listarTodos() {
		List<PessoaDTO> pessoas = pessoaRepository.findAll().stream()
			.map(this::converterParaDTO)
			.collect(Collectors.toList());
		return ResponseEntity.ok(pessoas);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PessoaDTO> buscarPorId(@PathVariable Long id) {
		return pessoaRepository.findById(id)
			.map(pessoa -> ResponseEntity.ok(converterParaDTO(pessoa)))
			.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/cpf/{cpf}")
	public ResponseEntity<PessoaDTO> buscarPorCpf(@PathVariable String cpf) {
		return pessoaRepository.findByCpf(cpf)
			.map(pessoa -> ResponseEntity.ok(converterParaDTO(pessoa)))
			.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaDTO pessoaDTO) {
		return pessoaRepository.findById(id)
			.map(pessoa -> {
				pessoa.setNome(pessoaDTO.getNome());
				pessoa.setEmail(pessoaDTO.getEmail());
				pessoa.setSenha(pessoaDTO.getSenha());
				pessoa.setDataNasc(pessoaDTO.getDataNasc());
				pessoa.setTipo(pessoaDTO.getTipo());
				pessoa.setSaldo(pessoaDTO.getSaldo());
				
				pessoa = pessoaRepository.save(pessoa);
				return ResponseEntity.ok(converterParaDTO(pessoa));
			})
			.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		return pessoaRepository.findById(id)
			.map(pessoa -> {
				pessoaRepository.delete(pessoa);
				return ResponseEntity.noContent().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
	}

	private PessoaDTO converterParaDTO(Pessoa pessoa) {
		PessoaDTO dto = new PessoaDTO();
		dto.setId(pessoa.getId());
		dto.setCpf(pessoa.getCpf());
		dto.setNome(pessoa.getNome());
		dto.setEmail(pessoa.getEmail());
		dto.setSenha(pessoa.getSenha());
		dto.setDataNasc(pessoa.getDataNasc());
		dto.setTipo(pessoa.getTipo());
		dto.setSaldo(pessoa.getSaldo());
		return dto;
	}
}
