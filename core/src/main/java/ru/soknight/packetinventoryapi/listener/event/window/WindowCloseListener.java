package ru.soknight.packetinventoryapi.listener.event.window;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.window.WindowCloseEvent;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface WindowCloseListener<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends EventListener<WindowCloseEvent<C, R>> {

    @Override
    default void handle(WindowCloseEvent<C, R> event) {
        handle(event.getActor(), event.getContainer());
    }
    
    void handle(Player actor, C container);
    
}
