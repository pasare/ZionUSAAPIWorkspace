package org.zionusa.base.util.validators;


import org.zionusa.base.util.constraints.TimeFormatConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeFormatValidator implements ConstraintValidator<TimeFormatConstraint, String> {

    @Override
    public boolean isValid(String time, ConstraintValidatorContext context) {
        if (time == null || time.equals(""))
            return true;

        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("h:mm a"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
