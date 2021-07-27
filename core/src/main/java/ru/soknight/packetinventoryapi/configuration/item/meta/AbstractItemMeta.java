package ru.soknight.packetinventoryapi.configuration.item.meta;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractItemMeta implements ConfigurationItemMeta {

    protected final ConfigurationItemType menuItemType;

}
