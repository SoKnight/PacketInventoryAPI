package ru.soknight.packetinventoryapi.listener.event.window;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.window.WindowPostCloseEvent;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface WindowPostCloseListener<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends EventListener<WindowPostCloseEvent<C, R>> {

    @Override
    default void handle(WindowPostCloseEvent<C, R> event) {
        handle(event.getActor(), event.getContainer());
    }
    
    void handle(Player actor, C container);
    
}
