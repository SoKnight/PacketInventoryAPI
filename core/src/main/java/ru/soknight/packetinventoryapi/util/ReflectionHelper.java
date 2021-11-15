package ru.soknight.packetinventoryapi.util;

import java.lang.reflect.Field;

public final class ReflectionHelper {

    public static boolean findFieldAndPutValue(Object instance, String fieldName, Object value) throws IllegalAccessException {
        Class<?> clazz = instance.getClass();

        while(clazz != null) {
            for(Field field : clazz.getDeclaredFields()) {
                if(field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    field.set(instance, value);
                    return true;
                }
            }

            clazz = clazz.getSuperclass();
        }

        return false;
    }

}
