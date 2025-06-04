package com.puc.moeda.dto;

import com.puc.moeda.interfaces.CPF;
import com.puc.moeda.interfaces.ValidEmail;
import com.puc.moeda.interfaces.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProfessorRegistrationDTO {

    @NotBlank(message = "O campo nome não pode estar vazio")
    private String name;

    @NotBlank(message = "O campo CPF não pode estar vazio")
    @CPF
    private String cpf;

    @NotBlank(message = "O campo departamento não pode estar vazio")
    private String department;

    @NotNull(message = "A instituição deve ser selecionada")
    private Long institutionId;

    @NotBlank(message = "O campo email não pode estar vazio")
    @ValidEmail
    private String email;

    @NotBlank(message = "O campo senha não pode estar vazio")
    @ValidPassword
    private String password;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    } 
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
