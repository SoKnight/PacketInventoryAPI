package ru.soknight.packetinventoryapi.listener.event.container;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.data.EnchantmentPosition;
import ru.soknight.packetinventoryapi.container.type.EnchantmentTableContainer;
import ru.soknight.packetinventoryapi.event.container.EnchantmentSelectEvent;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface EnchantmentSelectListener extends EventListener<EnchantmentSelectEvent> {

    @Override
    default void handle(EnchantmentSelectEvent event) {
        handle(event.getActor(), event.getContainer(), event.getEnchantmentPosition());
    }
    
    void handle(Player actor, EnchantmentTableContainer container, EnchantmentPosition position);
    
}
