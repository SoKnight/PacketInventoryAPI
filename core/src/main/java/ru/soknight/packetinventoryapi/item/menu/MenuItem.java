package ru.soknight.packetinventoryapi.item.menu;

import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public class MenuItem extends RenderableItem {

    private final int[] slots;
    private final ItemStack itemStack;
    private final Function<Player, ItemStack> itemRenderer;
    
    public MenuItem(int[] slots, ItemStack itemStack) {
        this(slots, itemStack, null);
    }
    
    public MenuItem(int[] slots, ItemStack itemStack, Function<Player, ItemStack> itemRenderer) {
        this.slots = slots;
        this.itemStack = itemStack;
        this.itemRenderer = itemRenderer;
    }
    
    @Override
    public ItemStack renderFor(Player player) {
        return itemRenderer != null ? itemRenderer.apply(player) : itemStack;
    }
    
}
