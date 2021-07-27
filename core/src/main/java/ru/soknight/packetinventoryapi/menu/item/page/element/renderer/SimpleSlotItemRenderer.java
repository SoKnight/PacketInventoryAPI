package ru.soknight.packetinventoryapi.menu.item.page.element.renderer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;

final class SimpleSlotItemRenderer implements SlotItemRenderer {

    @Override
    public ItemStack renderItem(Player viewer, RegularMenuItem<?, ?> menuItem, int slot, int pageIndex, int totalIndex) {
        return menuItem.asBukkitItemFor(viewer).clone();
    }

}
