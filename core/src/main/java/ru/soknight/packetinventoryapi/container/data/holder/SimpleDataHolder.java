package ru.soknight.packetinventoryapi.container.data.holder;

import lombok.Getter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static ru.soknight.packetinventoryapi.util.NumberConvertions.*;

@Getter
class SimpleDataHolder implements DataHolder {

    private final Map<String, Object> properties = new LinkedHashMap<>();

    @Override
    public int addInt(String key, int amount) {
        return addInt(key, amount, 0);
    }

    @Override
    public int addInt(String key, int amount, int def) {
        int value = getAsInt(key, def) + amount;
        set(key, value);
        return value;
    }

    @Override
    public long addLong(String key, long amount) {
        return addLong(key, amount, 0L);
    }

    @Override
    public long addLong(String key, long amount, long def) {
        long value = getAsLong(key, def) + amount;
        set(key, value);
        return value;
    }

    @Override
    public double addDouble(String key, double amount) {
        return addDouble(key, amount, 0D);
    }

    @Override
    public double addDouble(String key, double amount, double def) {
        double value = getAsDouble(key, def) + amount;
        set(key, value);
        return value;
    }

    @Override
    public float addFloat(String key, float amount) {
        return addFloat(key, amount, 0F);
    }

    @Override
    public float addFloat(String key, float amount, float def) {
        float value = getAsFloat(key, def) + amount;
        set(key, value);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T compute(String key, Class<T> valueType, BiFunction<? extends String, ? extends T, T> remappingFunction) {
        return (T) properties.compute(key, (BiFunction<? super String, ? super Object, ?>) remappingFunction);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) properties.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        return (T) properties.getOrDefault(key, defaultValue);
    }

    @Override
    public Integer getAsInt(String key) {
        return asInt(get(key));
    }

    @Override
    public int getAsInt(String key, int def) {
        return asInt(get(key), def);
    }

    @Override
    public Long getAsLong(String key) {
        return asLong(get(key));
    }

    @Override
    public long getAsLong(String key, long def) {
        return asLong(get(key), def);
    }

    @Override
    public Double getAsDouble(String key) {
        return asDouble(get(key));
    }

    @Override
    public double getAsDouble(String key, double def) {
        return asDouble(get(key), def);
    }

    @Override
    public Float getAsFloat(String key) {
        return asFloat(get(key));
    }

    @Override
    public float getAsFloat(String key, float def) {
        return asFloat(get(key), def);
    }

    @Override
    public boolean isSet(String key) {
        return properties.containsKey(key);
    }

    @Override
    public DataHolder set(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public Set<String> getKeys() {
        return properties.keySet();
    }

    @Override
    public Collection<Object> getValues() {
        return properties.values();
    }

}
