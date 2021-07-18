package ru.soknight.packetinventoryapi.listener.event.container;

import org.bukkit.entity.Player;

import ru.soknight.packetinventoryapi.container.type.StonecutterContainer;
import ru.soknight.packetinventoryapi.event.container.RecipeSelectEvent;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.util.PositionedSlot;

@FunctionalInterface
public interface RecipeSelectListener extends EventListener<RecipeSelectEvent> {

    @Override
    default void handle(RecipeSelectEvent event) {
        PositionedSlot slot = new PositionedSlot(event.getRow(), event.getColumn(), event.getSlot());
        handle(event.getActor(), event.getContainer(), slot);
    }
    
    void handle(Player actor, StonecutterContainer container, PositionedSlot slot);
    
}
