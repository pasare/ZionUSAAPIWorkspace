package org.zionusa.management.util;

public class ObjectUtil {

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
