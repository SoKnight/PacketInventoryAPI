package ru.soknight.packetinventoryapi.configuration.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.soknight.packetinventoryapi.menu.item.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.StateableMenuItem;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ConfigurationItemType {

    REGULAR(RegularMenuItem.class),
    STATEABLE(StateableMenuItem.class);

    private final Class<?> itemClass;

}
