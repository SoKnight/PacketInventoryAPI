package ru.soknight.packetinventoryapi.configuration.item.meta.page.element;

import lombok.Getter;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;
import ru.soknight.packetinventoryapi.configuration.item.meta.AbstractItemMeta;
import ru.soknight.packetinventoryapi.configuration.item.meta.ConfigurationItemMeta;

@Getter
final class SimplePageElementItemMeta extends AbstractItemMeta implements PageElementItemMeta {

    private final ConfigurationItemMeta elementPatternMeta;

    SimplePageElementItemMeta(ConfigurationItemMeta elementPatternMeta) {
        super(ConfigurationItemType.PAGE_ELEMENT);
        this.elementPatternMeta = elementPatternMeta;
    }

    @Override
    public String toString() {
        return "ConfigurationItemMeta{" +
                "menuItemType=" + menuItemType +
                ", elementPatternMeta=" + elementPatternMeta +
                '}';
    }

}
