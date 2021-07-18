package ru.soknight.packetinventoryapi.util;

public class Validate {

    private static final String NOT_NULL_FORMAT = "'%s' cannot be null!";
    private static final String NOT_EMPTY_FORMAT = "'%s' cannot be empty or null!";

    public static void notNull(Object var, String fieldName) {
        if(var == null)
            throw new IllegalArgumentException(String.format(NOT_NULL_FORMAT, fieldName));
    }

    public static void notEmpty(String var, String fieldName) {
        if(var == null || var.isEmpty())
            throw new IllegalArgumentException(String.format(NOT_EMPTY_FORMAT, fieldName));
    }

    public static void isTrue(boolean expression, String message) {
        if(!expression)
            throw new IllegalArgumentException(message);
    }

}
