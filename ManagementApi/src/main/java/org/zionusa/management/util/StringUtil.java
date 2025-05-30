package org.zionusa.management.util;

import java.text.Normalizer;

public class StringUtil {
    public static String sanitizeEmail(String value) {
        return sanitizeString(value).toLowerCase();
    }
    public static String sanitizeString(String value) {
        if (value == null) {
            return "";
        }
        return Normalizer
                // Normalizer.normalize will separate all of the accent marks from the characters
                .normalize(value, Normalizer.Form.NFD)
                // compare each character against being a letter and throw out the ones that aren't.
                .replaceAll("[^\\p{ASCII}]", "")
                // Remove spaces before or after the string
                .trim();
    }
}
