package com.puc.moeda.validator;

import com.puc.moeda.validation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidarSenha implements ConstraintValidator<ValidPassword, String> {
    
    @Override
    public boolean isValid(String senha, ConstraintValidatorContext context) {
        if (senha == null || senha.isEmpty()) {
            return false;
        }
        
        // Verifica se tem pelo menos 8 caracteres
        if (senha.length() < 8) {
            return false;
        }
        
        // Verifica se tem pelo menos uma letra maiúscula
        if (!senha.matches(".*[A-Z].*")) {
            return false;
        }
        
        // Verifica se tem pelo menos uma letra minúscula
        if (!senha.matches(".*[a-z].*")) {
            return false;
        }
        
        // Verifica se tem pelo menos um número
        if (!senha.matches(".*\\d.*")) {
            return false;
        }
        
        // Verifica se tem pelo menos um caractere especial
        if (!senha.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return false;
        }
        
        return true;
    }
} 