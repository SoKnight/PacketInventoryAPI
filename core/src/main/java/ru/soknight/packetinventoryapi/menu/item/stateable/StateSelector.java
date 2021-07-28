package ru.soknight.packetinventoryapi.menu.item.stateable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.menu.context.state.selector.StateSelectorContext;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;

@FunctionalInterface
public interface StateSelector<CTX extends StateSelectorContext> {

    ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    RegularMenuItem<?, ?> selectState(CTX context);

}
