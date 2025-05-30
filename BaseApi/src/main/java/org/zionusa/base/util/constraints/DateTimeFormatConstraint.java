package org.zionusa.base.util.constraints;

import org.zionusa.base.util.validators.DateTimeFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateTimeFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeFormatConstraint {
    String message() default "Invalid date format, dates should be provided in Standard ISO TIME Format (YYYY-MM-DD:hh:mm:ss)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
