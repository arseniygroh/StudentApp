package ukma.service.validation;

import ukma.domain.annotations.Length;
import java.lang.reflect.Field;

public class AnnotationValidator {

    public static void validate(Object obj) {
        Class<?> objClass = obj.getClass();

        while (objClass != null && objClass != Object.class) {

            for (Field field : objClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Length.class)) {
                    Length lengthAnnotation = field.getAnnotation(Length.class);
                    field.setAccessible(true);

                    try {
                        Object value = field.get(obj);

                        if (value instanceof String strValue) {
                            int currentLength = strValue.length();
                            int min = lengthAnnotation.min();
                            int max = lengthAnnotation.max();

                            if (currentLength < min || currentLength > max) {
                                throw new IllegalArgumentException(
                                        "Validation Failed: Field '" + field.getName() +
                                                "' length must be between " + min + " and " + max +
                                                ". Current length is " + currentLength
                                );
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Reflection error", e);
                    }
                }
            }
            objClass = objClass.getSuperclass();
        }
    }
}