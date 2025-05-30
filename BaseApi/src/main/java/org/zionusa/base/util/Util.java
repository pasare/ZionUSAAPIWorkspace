package org.zionusa.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Util {

    private Util() {
    }

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static Integer coalesce(Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    public static String coalesce(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static Object runGetter(Field field, Object o) {
        // MZ: Find the correct method
        for (Method method : getMethods(o.getClass())) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                    try {
                        return method.invoke(o);
                    } catch (IllegalAccessException e) {
                        logger.error("Could not determine method: " + method.getName(), e);
                    } catch (InvocationTargetException e) {
                        logger.error("Could not determine method: " + method.getName(), e);
                    }

                }
            }
        }


        return null;
    }

    public static Stack<Method> getMethods(Class type) {

        // MZ: Optionally, for performance reasons, cache the (non segmented) methods per type in a static map
        // MZ: this is just an example, and isn't threadsafe
        //if (classMethodCache.containsKey(type))
        //{
        //	return classMethodCache.get(type);
        //}

        Stack<Method> result = new Stack<Method>();
        try {
            for (Class<?> c = type; c != null; c = c.getSuperclass()) {
                Method[] methods = c.getDeclaredMethods();
                result.addAll(Arrays.asList(methods));
            }
        } catch (Exception e) {
            // MZ: Add your own logger instance here
            // Logger.error("Could not fetch object methods", e);
        }

        // MZ: Add to caching map
        // classMethodCache.put(type, result);

        return result;
    }

    public static Map<String, Object> getFieldsAndValues(List<String> columns, Object classObject) {
        Map<String, Object> result = new HashMap<>();
        for (String column : columns) {
            try {
                Object value = Util.runGetter(classObject.getClass().getDeclaredField(column), classObject);
                result.put(column, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String flattenToAscii(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        for (char c : string.toCharArray()) {
            if (c <= '\u007F') sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Get a diff between two dates
     *
     * @param date1    the oldest date
     * @param date2    the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        final long diffInMilliseconds = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
    }
}
