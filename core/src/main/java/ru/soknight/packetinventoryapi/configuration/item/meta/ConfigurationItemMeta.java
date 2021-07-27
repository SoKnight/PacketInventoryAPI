package ru.soknight.packetinventoryapi.configuration.item.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationStates;
import ru.soknight.packetinventoryapi.configuration.item.meta.page.element.PageElementItemMeta;
import ru.soknight.packetinventoryapi.configuration.item.meta.regular.RegularItemMeta;
import ru.soknight.packetinventoryapi.configuration.item.meta.stateable.StateableItemMeta;
import ru.soknight.packetinventoryapi.menu.item.page.element.PageElementMenuItem;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;
import ru.soknight.packetinventoryapi.util.Validate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface ConfigurationItemMeta {

    static @NotNull ConfigurationItemMeta create(@NotNull ConfigurationItemType menuItemType) {
        Validate.notNull(menuItemType, "menuItemType");
        return new SimpleConfigurationItemMeta(menuItemType);
    }

    static @Nullable ConfigurationItemMeta extractFromField(@NotNull Field field) {
        Class<?> fieldType = field.getType();

        ConfigurationStates configurationStates = field.getDeclaredAnnotation(ConfigurationStates.class);
        String[] states = configurationStates != null ? configurationStates.value() : new String[0];

        if(fieldType == RegularMenuItem.class) {
            return RegularItemMeta.create();
        } else if(fieldType == StateableMenuItem.class) {
            return StateableItemMeta.create(states);
        } else if(fieldType == PageElementMenuItem.class) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Type patternTypeRaw = genericType.getActualTypeArguments()[0];

            ConfigurationItemType patternType = ConfigurationItemType.fromItemClassType(patternTypeRaw);
            ConfigurationItemMeta patternMeta;

            if(patternType == ConfigurationItemType.REGULAR)
                patternMeta = RegularItemMeta.create();
            else if(patternType == ConfigurationItemType.STATEABLE)
                patternMeta = StateableItemMeta.create(states);
            else
                throw new IllegalStateException("unexpected pattern item type: " + patternType);

            return PageElementItemMeta.create(patternMeta);
        } else {
            return null;
        }
    }

    @NotNull ConfigurationItemType getMenuItemType();

    default @NotNull String[] resolveRequiredStates() {
        if(this instanceof StateableItemMeta)
            return ((StateableItemMeta) this).getRequiredStates();
        else
            return new String[0];
    }

    default @NotNull ConfigurationItemMeta resolveElementPatternMeta() {
        if(this instanceof PageElementItemMeta)
            return ((PageElementItemMeta) this).getElementPatternMeta();
        else
            return RegularItemMeta.create();
    }

    default @NotNull ConfigurationItemType resolveElementPatternType() {
        return resolveElementPatternMeta().getMenuItemType();
    }

}
