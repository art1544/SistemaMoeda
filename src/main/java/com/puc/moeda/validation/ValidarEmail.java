package com.puc.moeda.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidarEmail implements ConstraintValidator<Email, String> {
    
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        // Regex para validar email
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        
        // Verifica se o email corresponde ao padrão
        if (!email.matches(regex)) {
            return false;
        }
        
        // Verifica se o domínio tem pelo menos um ponto
        String[] partes = email.split("@");
        if (partes.length != 2 || !partes[1].contains(".")) {
            return false;
        }
        
        // Verifica se não há espaços
        if (email.contains(" ")) {
            return false;
        }
        
        // Verifica se o tamanho é razoável
        if (email.length() > 254) {
            return false;
        }
        
        return true;
    }
} 