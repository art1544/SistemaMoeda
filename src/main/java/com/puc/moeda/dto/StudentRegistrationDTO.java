package com.puc.moeda.dto;

import com.puc.moeda.interfaces.CPF;
import com.puc.moeda.interfaces.ValidEmail;
import com.puc.moeda.interfaces.ValidPassword;
import com.puc.moeda.validator.PasswordMatch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@PasswordMatch // Apply the custom password match validator at the class level
public class StudentRegistrationDTO {

    @NotBlank(message = "O campo nome não pode estar vazio")
    private String name;

    @NotBlank(message = "O campo email não pode estar vazio")
    @ValidEmail
    private String email;

    @NotBlank(message = "O campo CPF não pode estar vazio")
    @CPF
    private String cpf;

    @NotBlank(message = "O campo RG não pode estar vazio")
    private String rg;

    @NotBlank(message = "O campo endereço não pode estar vazio")
    private String address;

    @NotNull(message = "A instituição de ensino deve ser selecionada")
    private Long institutionId;

    @NotBlank(message = "O campo curso não pode estar vazio")
    private String course;

    @NotBlank(message = "O campo senha não pode estar vazio")
    @ValidPassword
    private String password;

    @NotBlank(message = "O campo de confirmação de senha não pode estar vazio")
    private String confirmPassword;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
