package com.RESTfullApi.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidAge {
   String message() default "Invalid age, must be 18 or older";
   Class<?>[] groups() default {};
   Class<? extends Payload>[] payload() default {};
}
