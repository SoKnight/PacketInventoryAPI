package ru.soknight.packetinventoryapi.menu.item.regular.renderer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RegularItemRenderer {

    RegularItemRenderer DEFAULT = (viewer, item, slot, slotIndex) -> item;

    ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    @NotNull ItemStack renderItem(Player viewer, ItemStack item, int slot, int slotIndex);

}
