package com.RESTfullApi.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null) {
            return false;
        }

        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }
}
