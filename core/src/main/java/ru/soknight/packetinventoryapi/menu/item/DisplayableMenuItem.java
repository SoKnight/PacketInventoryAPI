package ru.soknight.packetinventoryapi.menu.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;

public interface DisplayableMenuItem extends MenuItem {

    @NotNull ItemStack asBukkitItemFor(Player viewer);

    @NotNull RegularMenuItem<?, ?> getItemFor(Player viewer);

}
