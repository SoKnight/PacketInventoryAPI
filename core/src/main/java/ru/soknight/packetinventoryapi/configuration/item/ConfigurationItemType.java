package ru.soknight.packetinventoryapi.configuration.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.soknight.packetinventoryapi.menu.item.page.element.PageElementMenuItem;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;

import java.lang.reflect.Type;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ConfigurationItemType {

    REGULAR(RegularMenuItem.class),
    STATEABLE(StateableMenuItem.class),
    PAGE_ELEMENT(PageElementMenuItem.class);

    private final Type itemClassType;

    public static ConfigurationItemType fromItemClassType(Type itemClassType) {
        for(ConfigurationItemType itemType : values())
            if(itemType.getItemClassType() == itemClassType)
                return itemType;

        return REGULAR;
    }

}
