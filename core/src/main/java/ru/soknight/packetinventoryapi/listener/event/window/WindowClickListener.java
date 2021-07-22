package ru.soknight.packetinventoryapi.listener.event.window;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.event.window.WindowClickEvent;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface WindowClickListener<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends EventListener<WindowClickEvent<C, R>> {

    @Override
    default void handle(WindowClickEvent<C, R> event) {
        handle(event.getActor(), event.getContainer(), event.getClickedSlot(), event.getClickType(), event.getClickedItem());
    }
    
    void handle(Player actor, C container, int clickedSlot, WindowClickType clickType, ItemStack clickedItem);
    
}
