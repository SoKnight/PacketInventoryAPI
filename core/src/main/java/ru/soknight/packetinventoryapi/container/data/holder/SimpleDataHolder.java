package ru.soknight.packetinventoryapi.container.data.holder;

import lombok.Getter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Getter
class SimpleDataHolder implements DataHolder {

    private final Map<String, Object> properties = new LinkedHashMap<>();

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
