package ru.soknight.packetinventoryapi.configuration.item.meta.regular;

import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;
import ru.soknight.packetinventoryapi.configuration.item.meta.ConfigurationItemMeta;

public interface RegularItemMeta extends ConfigurationItemMeta {

    static @NotNull ConfigurationItemMeta create() {
        return ConfigurationItemMeta.create(ConfigurationItemType.REGULAR);
    }

}
