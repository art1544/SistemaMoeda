package com.puc.moeda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puc.moeda.dto.LoginDTO;
import com.puc.moeda.dto.PessoaDTO;
import com.puc.moeda.model.Pessoa;
import com.puc.moeda.repository.PessoaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @PostMapping("/login")
    public ResponseEntity<PessoaDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return pessoaRepository.findByEmail(loginDTO.getEmail())
            .filter(pessoa -> pessoa.getSenha().equals(loginDTO.getSenha()))
            .map(pessoa -> ResponseEntity.ok(converterParaDTO(pessoa)))
            .orElse(ResponseEntity.badRequest().build());
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
