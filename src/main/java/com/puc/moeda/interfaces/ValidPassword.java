package com.puc.moeda.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.puc.moeda.validator.ValidPasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Senha deve ter pelo menos 8 caracteres";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
