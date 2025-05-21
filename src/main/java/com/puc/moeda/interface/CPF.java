@Constraint(validatedBy = ValidarCPF.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CPF {
String message() default "CPF invalido, escreva um CPF valido";
Class <?> [] groups() default {};
Class<? extends Payload> [] payload() default {}
}

