package ru.soknight.packetinventoryapi.listener.event.window;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.window.WindowContentLoadEvent;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface WindowContentLoadListener<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends EventListener<WindowContentLoadEvent<C, R>> {

    @Override
    default void handle(WindowContentLoadEvent<C, R> event) {
        handle(event.getActor(), event.getContainer());
    }
    
    void handle(Player actor, C container);
    
}
