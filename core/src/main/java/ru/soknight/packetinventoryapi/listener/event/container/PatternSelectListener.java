package ru.soknight.packetinventoryapi.listener.event.container;

import org.bukkit.entity.Player;

import ru.soknight.packetinventoryapi.container.type.LoomContainer;
import ru.soknight.packetinventoryapi.event.container.PatternSelectEvent;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.util.PositionedSlot;

@FunctionalInterface
public interface PatternSelectListener extends EventListener<PatternSelectEvent> {

    @Override
    default void handle(PatternSelectEvent event) {
        PositionedSlot slot = new PositionedSlot(event.getRow(), event.getColumn(), event.getSlot());
        handle(event.getActor(), event.getContainer(), slot);
    }
    
    void handle(Player actor, LoomContainer container, PositionedSlot slot);
    
}
