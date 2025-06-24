package com.puc.moeda.dto;

import com.puc.moeda.interfaces.ValidEmail;
import com.puc.moeda.interfaces.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "O campo usuário/email não pode estar vazio")
    @ValidEmail(message = "Formato de email inválido")
    private String usernameOrEmail;

    @NotBlank(message = "O campo senha não pode estar vazio")
    @ValidPassword
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
