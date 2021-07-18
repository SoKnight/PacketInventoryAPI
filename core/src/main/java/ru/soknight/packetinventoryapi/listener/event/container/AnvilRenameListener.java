package ru.soknight.packetinventoryapi.listener.event.container;

import org.bukkit.entity.Player;

import ru.soknight.packetinventoryapi.container.type.AnvilContainer;
import ru.soknight.packetinventoryapi.event.container.AnvilRenameEvent;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface AnvilRenameListener extends EventListener<AnvilRenameEvent> {

    @Override
    default void handle(AnvilRenameEvent event) {
        handle(event.getActor(), event.getContainer(), event.getCustomName());
    }
    
    void handle(Player actor, AnvilContainer container, String text);
    
}
