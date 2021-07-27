package ru.soknight.packetinventoryapi.configuration.item.meta.stateable;

import lombok.Getter;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;
import ru.soknight.packetinventoryapi.configuration.item.meta.AbstractItemMeta;

import java.util.Arrays;

@Getter
final class SimpleStateableItemMeta extends AbstractItemMeta implements StateableItemMeta {

    private final String[] requiredStates;

    SimpleStateableItemMeta(String[] requiredStates) {
        super(ConfigurationItemType.STATEABLE);
        this.requiredStates = requiredStates;
    }

    @Override
    public String toString() {
        return "ConfigurationItemMeta{" +
                "menuItemType=" + menuItemType +
                ", requiredStates=" + Arrays.toString(requiredStates) +
                '}';
    }

}
