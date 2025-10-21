/*
package com.klasix12.annotation;

import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

public class ValidListValidator implements ConstraintValidator<ValidList, List<AnswerRequest>> {
    private Validator validator;
    private Class<?>[] groups;

    @Override
    public void initialize(ValidList constraintAnnotation) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.groups = constraintAnnotation.groups();
    }

    @Override
    public boolean isValid(List<AnswerRequest> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        boolean isValid = true;
        for (int i = 0; i < value.size(); i++) {
            Object obj = value.get(i);
            Set<ConstraintViolation<Object>> violations = validator.validate(obj, groups);
            if (!violations.isEmpty()) {
                isValid = false;
                context.disableDefaultConstraintViolation();
                for (ConstraintViolation<Object> violation : violations) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                            violation.getPropertyPath() + ": " + violation.getMessage()
                    ).addConstraintViolation();
                }

            }
        }
        return isValid;
    }
}
*/
