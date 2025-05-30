package org.zionusa.base.util.validators;

import org.zionusa.base.util.constraints.DateTimeFormatConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeFormatValidator implements ConstraintValidator<DateTimeFormatConstraint, String> {

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null || date.equals(""))
            return true;

        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE_TIME);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
