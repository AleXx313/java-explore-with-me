package ru.practicum.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateValidator.class)
public @interface StartAfterTwoHours {
    String message() default "Start must be not earlier than two hours after event creation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
