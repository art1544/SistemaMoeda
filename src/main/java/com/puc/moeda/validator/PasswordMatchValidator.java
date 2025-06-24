package com.puc.moeda.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.puc.moeda.dto.StudentRegistrationDTO;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, StudentRegistrationDTO> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
    }

    @Override
    public boolean isValid(StudentRegistrationDTO studentRegistrationDTO, ConstraintValidatorContext context) {
        if (studentRegistrationDTO.getPassword() == null || studentRegistrationDTO.getConfirmPassword() == null) {
            return false;
        }
        return studentRegistrationDTO.getPassword().equals(studentRegistrationDTO.getConfirmPassword());
    }
}
