package com.gymtutor.gymtutor.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMatch {
    String message() default "Os campos não coincidem.";

    String first();  // nome do primeiro campo
    String second(); // nome do segundo campo

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}