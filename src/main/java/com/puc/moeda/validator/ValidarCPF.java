package com.puc.moeda.validator;

import com.puc.moeda.interfaces.CPF;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidarCPF implements ConstraintValidator<CPF, String> {
    @Override
    public void initialize(CPF constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return true;
        }

        cpf = cpf.replace(".", "").replace("-", "");

        if (cpf.length() == 11) {
            return true;
        } else {
            return false;
        }
    }
}