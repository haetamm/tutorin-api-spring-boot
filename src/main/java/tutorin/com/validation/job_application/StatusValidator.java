package tutorin.com.validation.job_application;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tutorin.com.annotation.job_application.ValidStatus;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {
    private Enum<?>[] enumValues;

    @Override
    public void initialize(ValidStatus annotation) {
        this.enumValues = annotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (Enum<?> enumValue : enumValues) {
            if (enumValue.name().equalsIgnoreCase(value)) {
                return true;
            }
        }

        return false;
    }
}


