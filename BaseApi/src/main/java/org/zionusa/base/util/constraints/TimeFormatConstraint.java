package org.zionusa.base.util.constraints;
import org.zionusa.base.util.validators.TimeFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeFormatValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeFormatConstraint {
    String message() default "Invalid time format, times should be provided in US LOCAL TIME (h:mm a)";
    Class<?> [] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
