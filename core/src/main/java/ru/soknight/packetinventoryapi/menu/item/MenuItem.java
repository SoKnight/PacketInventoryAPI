package ru.soknight.packetinventoryapi.menu.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface MenuItem {

    @NotNull ItemStack asBukkitItemFor(Player viewer);

    @NotNull RegularMenuItem<?, ?> getItemFor(Player viewer);

    @SuppressWarnings("unchecked")
    default <I extends RegularMenuItem<I, B>, B extends RegularMenuItem.Builder<I, B>> RegularMenuItem<I, B> asRegularItem() {
        return (RegularMenuItem<I, B>) this;
    }

    default StateableMenuItem asStateableItem() {
        return (StateableMenuItem) this;
    }

    default boolean isRegular() {
        return this instanceof RegularMenuItem;
    }

    default boolean isStateable() {
        return this instanceof StateableMenuItem;
    }

}
