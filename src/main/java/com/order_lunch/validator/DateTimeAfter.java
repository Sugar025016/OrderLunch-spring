package com.order_lunch.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = DateTimeAfterValidator.class)
public @interface DateTimeAfter {
    String message() default "Date should be at least one hour after the current time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {}; // 定義有效負載
}

class DateTimeAfterValidator implements ConstraintValidator<DateTimeAfter, LocalDateTime> {

    @Override
    public void initialize(DateTimeAfter constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);
        return value.isAfter(oneHourLater);
    }
}