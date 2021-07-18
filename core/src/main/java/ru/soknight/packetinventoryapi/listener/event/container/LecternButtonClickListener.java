package ru.soknight.packetinventoryapi.listener.event.container;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.data.LecternButtonType;
import ru.soknight.packetinventoryapi.container.type.LecternContainer;
import ru.soknight.packetinventoryapi.event.container.LecternButtonClickEvent;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface LecternButtonClickListener extends EventListener<LecternButtonClickEvent> {

    @Override
    default void handle(LecternButtonClickEvent event) {
        handle(event.getActor(), event.getContainer(), event.getButtonType());
    }
    
    void handle(Player actor, LecternContainer container, LecternButtonType buttonType);
    
}
