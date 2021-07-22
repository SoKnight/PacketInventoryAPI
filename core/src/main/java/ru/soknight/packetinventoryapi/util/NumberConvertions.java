package ru.soknight.packetinventoryapi.util;

public final class NumberConvertions {

    public static Integer asInt(Object object) {
        if(object == null)
            return null;

        if(object instanceof Number)
            return ((Number) object).intValue();

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static int asInt(Object object, int def) {
        if(object == null)
            return def;

        if(object instanceof Number)
            return ((Number) object).intValue();

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException ignored) {
            return def;
        }
    }

    public static Long asLong(Object object) {
        if(object == null)
            return null;

        if(object instanceof Number)
            return ((Number) object).longValue();

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static long asLong(Object object, long def) {
        if(object == null)
            return def;

        if(object instanceof Number)
            return ((Number) object).longValue();

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException ignored) {
            return def;
        }
    }

    public static Double asDouble(Object object) {
        if(object == null)
            return null;

        if(object instanceof Number)
            return ((Number) object).doubleValue();

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static double asDouble(Object object, double def) {
        if(object == null)
            return def;

        if(object instanceof Number)
            return ((Number) object).doubleValue();

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException ignored) {
            return def;
        }
    }

    public static Float asFloat(Object object) {
        if(object == null)
            return null;

        if(object instanceof Number)
            return ((Number) object).floatValue();

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static float asFloat(Object object, float def) {
        if(object == null)
            return def;

        if(object instanceof Number)
            return ((Number) object).floatValue();

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException ignored) {
            return def;
        }
    }

}
