package com.puc.moeda.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.puc.moeda.validator.ValidarSenha;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ValidarSenha.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "A senha deve ter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma minúscula, um número e um caractere especial";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 