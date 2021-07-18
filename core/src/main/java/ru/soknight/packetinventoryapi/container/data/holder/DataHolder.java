package ru.soknight.packetinventoryapi.container.data.holder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface DataHolder {

    static DataHolder create() {
        return new SimpleDataHolder();
    }

    <T> T get(String key);

    <T> T get(String key, T defaultValue);

    boolean isSet(String key);

    DataHolder set(String key, Object value);

    Map<String, Object> getProperties();

    Set<String> getKeys();

    Collection<Object> getValues();

}
