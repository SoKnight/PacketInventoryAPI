package ru.soknight.packetinventoryapi.container.data.holder;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface DataHolder {

    static @NotNull DataHolder create() {
        return new SimpleDataHolder();
    }

    <T> T compute(String key, Class<T> valueType, BiFunction<? extends String, ? extends T, T> remappingFunction);
    <T> T computeIfAbsent(String key, Class<T> valueType, Function<? extends String, T> mappingFunction);
    <T> T computeIfPresent(String key, Class<T> valueType, BiFunction<? extends String, ? extends T, T> remappingFunction);

    int addInt(String key, int amount);
    int addInt(String key, int amount, int def);

    long addLong(String key, long amount);
    long addLong(String key, long amount, long def);

    double addDouble(String key, double amount);
    double addDouble(String key, double amount, double def);

    float addFloat(String key, float amount);
    float addFloat(String key, float amount, float def);

    <T> T get(String key);
    <T> T get(String key, T defaultValue);

    Integer getAsInt(String key);
    int getAsInt(String key, int def);

    Long getAsLong(String key);
    long getAsLong(String key, long def);

    Double getAsDouble(String key);
    double getAsDouble(String key, double def);

    Float getAsFloat(String key);
    float getAsFloat(String key, float def);

    boolean isSet(String key);

    DataHolder set(String key, Object value);

    Map<String, Object> getProperties();

    Set<String> getKeys();

    Collection<Object> getValues();

}
