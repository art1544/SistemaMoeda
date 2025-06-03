package com.puc.moeda.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.puc.moeda.interfaces.ValidPassword;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    // Simplified regex: Requires at least 8 characters
    private static final String PASSWORD_PATTERN = ".{8,}"; // Simplified regex

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) { // Handle null case, @NotBlank handles empty
            return false;
        }
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // Removed the private validatePassword method as logic is now directly in isValid
}
