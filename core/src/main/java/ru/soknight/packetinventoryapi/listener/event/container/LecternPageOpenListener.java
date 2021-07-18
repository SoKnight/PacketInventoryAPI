package ru.soknight.packetinventoryapi.listener.event.container;

import org.bukkit.entity.Player;

import ru.soknight.packetinventoryapi.container.type.LecternContainer;
import ru.soknight.packetinventoryapi.event.container.LecternPageOpenEvent;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface LecternPageOpenListener extends EventListener<LecternPageOpenEvent> {

    @Override
    default void handle(LecternPageOpenEvent event) {
        handle(event.getActor(), event.getContainer(), event.getPage());
    }
    
    void handle(Player actor, LecternContainer container, int page);
    
}
