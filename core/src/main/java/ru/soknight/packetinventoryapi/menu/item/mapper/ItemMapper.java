package ru.soknight.packetinventoryapi.menu.item.mapper;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ItemMapper {

    ItemMapper DEFAULT = (itemStack, player, slot) -> itemStack;

    @NotNull ItemStack apply(@NotNull ItemStack itemStack, @NotNull Player player, int slot);

}
