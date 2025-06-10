package com.puc.moeda.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.puc.moeda.interfaces.ValidEmail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false; // @NotBlank já trata o caso de nulo, então retornamos false aqui
        }
        return validateEmail(email);
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
