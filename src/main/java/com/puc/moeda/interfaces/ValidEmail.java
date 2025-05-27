package com.puc.moeda.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.puc.moeda.validator.ValidEmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String message() default "Formato de email inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
