package ru.soknight.packetinventoryapi.configuration.item;

import ru.soknight.packetinventoryapi.menu.Menu;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.item.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.StateableMenuItem;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationItemStructure<M extends Menu<?, ?>> {

    private final M menuInstance;
    private final Class<M> menuType;

    private final Map<String, Field> itemFields;
    private final Map<String, ConfigurationItemType> itemTypes;

    @SuppressWarnings("unchecked")
    public ConfigurationItemStructure(M menuInstance) {
        this.menuInstance = menuInstance;
        this.menuType = (Class<M>) menuInstance.getClass();

        this.itemFields = new HashMap<>();
        this.itemTypes = new LinkedHashMap<>();

        fetchItemFields();
    }

    private void fetchItemFields() {
        Field[] fields = menuType.getDeclaredFields();
        for(Field field : fields) {
            ConfigurationItem configurationItem = field.getAnnotation(ConfigurationItem.class);
            if(configurationItem == null)
                continue;

            String name = configurationItem.value();
            if(name == null || name.isEmpty())
                name = field.getName();

            Class<?> fieldType = field.getType();
            ConfigurationItemType itemType;

            if(fieldType == RegularMenuItem.class)
                itemType = ConfigurationItemType.REGULAR;
            else if(fieldType == StateableMenuItem.class)
                itemType = ConfigurationItemType.STATEABLE;
            else
                continue;

            itemFields.put(name, field);
            itemTypes.put(name, itemType);
        }
    }

    public void fillItemFields(Map<String, MenuItem> menuItems) {
        if(itemFields.isEmpty())
            return;

        itemFields.forEach((id, field) -> fillItemField(field, menuItems.get(id)));
    }

    private void fillItemField(Field field, MenuItem value) {
        try {
            field.setAccessible(true);
            field.set(menuInstance, value);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public ConfigurationItemStructure<M> addItem(String id, ConfigurationItemType itemType) {
        itemTypes.putIfAbsent(id, itemType);
        return this;
    }

    public ConfigurationItemStructure<M> addRegularItem(String id) {
        return addItem(id, ConfigurationItemType.REGULAR);
    }

    public ConfigurationItemStructure<M> addStateableItem(String id) {
        return addItem(id, ConfigurationItemType.STATEABLE);
    }

    public ConfigurationItemType getItemType(String id) {
        return itemTypes.getOrDefault(id, ConfigurationItemType.REGULAR);
    }

    public Map<String, ConfigurationItemType> getItemTypes() {
        return Collections.unmodifiableMap(itemTypes);
    }

}
