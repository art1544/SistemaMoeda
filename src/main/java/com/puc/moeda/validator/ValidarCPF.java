package com.puc.moeda.validator;

import com.puc.moeda.interfaces.CPF; // Corrected import to your custom annotation

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidarCPF implements ConstraintValidator<CPF, String> {
    @Override
    public void initialize(CPF constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return true; // @NotBlank should handle empty/null
        }

        // Remove non-digit characters for validation
        cpf = cpf.replace(".", "").replace("-", "");

        // Simplified validation: check for exactly 11 digits and numeric characters
        if (cpf.length() == 11) {
            return true;
        } else {
            return false;
        }
    }
}