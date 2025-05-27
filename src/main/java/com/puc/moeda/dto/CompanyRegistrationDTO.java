package com.puc.moeda.dto;

import com.puc.moeda.interfaces.ValidEmail;
import com.puc.moeda.interfaces.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public class CompanyRegistrationDTO {

    @NotBlank(message = "O campo nome da empresa não pode estar vazio")
    private String name;

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
