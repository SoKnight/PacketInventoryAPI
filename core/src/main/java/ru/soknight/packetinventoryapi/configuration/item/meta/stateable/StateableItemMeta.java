package ru.soknight.packetinventoryapi.configuration.item.meta.stateable;

import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.configuration.item.meta.ConfigurationItemMeta;
import ru.soknight.packetinventoryapi.util.Validate;

public interface StateableItemMeta extends ConfigurationItemMeta {

    static @NotNull StateableItemMeta create(@NotNull String... requiredStates) {
        Validate.notNull(requiredStates, "requiredStates");
        return new SimpleStateableItemMeta(requiredStates);
    }

    @NotNull String[] getRequiredStates();

}
