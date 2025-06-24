package com.puc.moeda.dto;

import com.puc.moeda.interfaces.ValidEmail;
import jakarta.validation.constraints.NotBlank;

public class PasswordRecoveryRequestDTO {

    @NotBlank(message = "O campo email não pode estar vazio")
    @ValidEmail
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
