package ru.soknight.packetinventoryapi.listener.event.window;

import org.bukkit.entity.Player;

import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.window.WindowOpenEvent;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface WindowOpenListener<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends EventListener<WindowOpenEvent<C, R>> {

    @Override
    default void handle(WindowOpenEvent<C, R> event) {
        handle(event.getActor(), event.getContainer());
    }
    
    void handle(Player actor, C container);
    
}
