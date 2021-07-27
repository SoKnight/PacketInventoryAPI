package ru.soknight.packetinventoryapi.menu.item.stateable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;

@FunctionalInterface
public interface StateSelector {

    ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    RegularMenuItem<?, ?> selectState(StateableMenuItem item, Player viewer);

}
