package com.bookingapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CheckInDateBeforeValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckInDateBefore {
    String[] fields();

    String message() default "Field checkIn must be earlier then checkOut, at least for one day";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}