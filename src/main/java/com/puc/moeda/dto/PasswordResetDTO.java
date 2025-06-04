package com.puc.moeda.dto;

import com.puc.moeda.interfaces.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetDTO {

    @NotBlank(message = "O campo token não pode estar vazio")
    private String token;

    @NotBlank(message = "O campo nova senha não pode estar vazio")
    @ValidPassword
    private String newPassword;

    @NotBlank(message = "O campo de confirmação de nova senha não pode estar vazio")
    private String confirmNewPassword;

    // Getters and setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
