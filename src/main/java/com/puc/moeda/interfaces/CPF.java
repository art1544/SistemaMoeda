package com.puc.moeda.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.puc.moeda.validator.ValidarCPF;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ValidarCPF.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CPF {
    String message() default "CPF invalido, escreva um CPF valido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}