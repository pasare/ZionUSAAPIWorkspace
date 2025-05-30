package org.zionusa.base.util.constraints;

import org.zionusa.base.util.validators.DateFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormatConstraint {
    String message() default "Invalid date format, dates should be provided in Standard ISO Format (YYYY-MM-DD)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
