package com.puc.moeda.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.puc.moeda.validation.CPF;

public class ValidarCPF implements ConstraintValidator<CPF, String> {
    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) 
            return false;
        
        if (cpf.chars().distinct().count() == 1) 
            return false;

        // Validação dos dígitos verificadores
        int[] pesosPrimeiroDigito = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesosSegundoDigito = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        // Cálculo do primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * pesosPrimeiroDigito[i];
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) primeiroDigito = 0;

        // Cálculo do segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * pesosSegundoDigito[i];
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) segundoDigito = 0;

        // Verifica se os dígitos calculados são iguais aos dígitos do CPF
        return cpf.charAt(9) == Character.forDigit(primeiroDigito, 10) &&
               cpf.charAt(10) == Character.forDigit(segundoDigito, 10);
    }
}
