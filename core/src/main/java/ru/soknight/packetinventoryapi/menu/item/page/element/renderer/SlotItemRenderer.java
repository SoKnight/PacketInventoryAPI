package ru.soknight.packetinventoryapi.menu.item.page.element.renderer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;

@FunctionalInterface
public interface SlotItemRenderer {

    SlotItemRenderer DEFAULT_CLONING = new SimpleSlotItemRenderer();

    ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    ItemStack renderItem(Player viewer, RegularMenuItem<?, ?> menuItem, int slot, int pageIndex, int totalIndex);

}
