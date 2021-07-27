package ru.soknight.packetinventoryapi.menu.item;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.menu.item.page.element.PageElementMenuItem;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;

public interface MenuItem {

    @Nullable ConfigurationSection getConfiguration();

    @SuppressWarnings("unchecked")
    default <I extends RegularMenuItem<I, B>, B extends RegularMenuItem.Builder<I, B>> RegularMenuItem<I, B> asRegularItem() {
        return (RegularMenuItem<I, B>) this;
    }

    default StateableMenuItem asStateableItem() {
        return (StateableMenuItem) this;
    }

    @SuppressWarnings("unchecked")
    default <I extends DisplayableMenuItem> PageElementMenuItem<I> asPageElementItem() {
        return (PageElementMenuItem<I>) this;
    }

    default boolean isRegular() {
        return this instanceof RegularMenuItem;
    }

    default boolean isStateable() {
        return this instanceof StateableMenuItem;
    }

    default boolean isPageElement() {
        return this instanceof PageElementMenuItem;
    }

}
