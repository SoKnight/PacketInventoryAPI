package ru.soknight.packetinventoryapi.configuration.item.meta.page.element;

import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;
import ru.soknight.packetinventoryapi.configuration.item.meta.ConfigurationItemMeta;
import ru.soknight.packetinventoryapi.util.Validate;

public interface PageElementItemMeta extends ConfigurationItemMeta {

    static @NotNull PageElementItemMeta create(@NotNull ConfigurationItemMeta elementPatternMeta) {
        Validate.notNull(elementPatternMeta, "elementPatternMeta");

        if(elementPatternMeta.getMenuItemType() == ConfigurationItemType.PAGE_ELEMENT)
            throw new IllegalArgumentException("'elementPatternMeta' cannot be a page element meta again!");

        return new SimplePageElementItemMeta(elementPatternMeta);
    }

    @NotNull ConfigurationItemMeta getElementPatternMeta();

}
