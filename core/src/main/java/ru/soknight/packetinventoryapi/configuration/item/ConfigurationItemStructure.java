package ru.soknight.packetinventoryapi.configuration.item;

import ru.soknight.packetinventoryapi.configuration.item.meta.ConfigurationItemMeta;
import ru.soknight.packetinventoryapi.configuration.item.meta.page.element.PageElementItemMeta;
import ru.soknight.packetinventoryapi.configuration.item.meta.regular.RegularItemMeta;
import ru.soknight.packetinventoryapi.configuration.item.meta.stateable.StateableItemMeta;
import ru.soknight.packetinventoryapi.menu.Menu;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;

import java.lang.reflect.Field;
import java.util.*;

public class ConfigurationItemStructure<M extends Menu<?, ?>> {

    private final M menuInstance;
    private final Class<M> menuType;

    private final Map<String, Field> itemFields;
    private final Map<String, ConfigurationItemMeta> itemMetaMap;

    @SuppressWarnings("unchecked")
    public ConfigurationItemStructure(M menuInstance) {
        this.menuInstance = menuInstance;
        this.menuType = (Class<M>) menuInstance.getClass();

        this.itemFields = new HashMap<>();
        this.itemMetaMap = new LinkedHashMap<>();

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

            ConfigurationItemMeta itemMeta = ConfigurationItemMeta.extractFromField(field);

            itemFields.put(name, field);
            itemMetaMap.put(name, itemMeta);
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

    public ConfigurationItemStructure<M> addItem(String id, ConfigurationItemMeta itemMeta) {
        itemMetaMap.putIfAbsent(id, itemMeta);
        return this;
    }

    public ConfigurationItemStructure<M> addRegularItem(String id) {
        return addItem(id, RegularItemMeta.create());
    }

    public ConfigurationItemStructure<M> addStateableItem(String id, String... requiredStates) {
        return addItem(id, StateableItemMeta.create(requiredStates != null ? requiredStates : new String[0]));
    }

    public ConfigurationItemStructure<M> addPageElementItem(String id, ConfigurationItemMeta patternItemMeta) {
        return addItem(id, PageElementItemMeta.create(patternItemMeta));
    }

    public ConfigurationItemMeta getItemMeta(String id) {
        return itemMetaMap.get(id);
    }

    public ConfigurationItemType getItemType(String id) {
        return Optional.ofNullable(getItemMeta(id))
                .map(ConfigurationItemMeta::getMenuItemType)
                .orElse(ConfigurationItemType.REGULAR);
    }

    public Map<String, ConfigurationItemMeta> getItemMetaMap() {
        return Collections.unmodifiableMap(itemMetaMap);
    }

}
