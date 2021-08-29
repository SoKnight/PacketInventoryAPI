package ru.soknight.packetinventoryapi.menu.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;

public interface DisplayableMenuItem extends MenuItem {

    @NotNull ItemStack asBukkitItemFor(Player viewer, int slot);

    @NotNull RegularMenuItem<?, ?> getItemFor(Player viewer, int slot);

    @NotNull int[] getSlots();

    @Nullable FillPatternType getFillPattern();

}
