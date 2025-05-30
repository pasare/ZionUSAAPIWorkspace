package org.zionusa.admin.util.validators;

import org.zionusa.admin.util.constraints.DateFormatConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<DateFormatConstraint, String> {

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null || date.equals(""))
            return true;

        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
