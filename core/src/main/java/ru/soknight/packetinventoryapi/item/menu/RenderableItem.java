package ru.soknight.packetinventoryapi.item.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class RenderableItem {

    public abstract ItemStack renderFor(Player player);
    
}
