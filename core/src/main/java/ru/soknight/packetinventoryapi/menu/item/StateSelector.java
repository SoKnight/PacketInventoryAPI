package ru.soknight.packetinventoryapi.menu.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface StateSelector {

    ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    RegularMenuItem<?, ?> selectState(StateableMenuItem item, Player viewer);

}
