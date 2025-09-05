package com.github.henrikac.weblogparser.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidLogFileValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLogFile {
    String message() default "File must be a non-empty .log file";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
