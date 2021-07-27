package ru.soknight.packetinventoryapi.configuration.item.meta;

import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;

final class SimpleConfigurationItemMeta extends AbstractItemMeta {

    SimpleConfigurationItemMeta(ConfigurationItemType menuItemType) {
        super(menuItemType);
    }

    @Override
    public String toString() {
        return "ConfigurationItemMeta{" +
                "menuItemType=" + menuItemType +
                '}';
    }

}
