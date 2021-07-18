package ru.soknight.packetinventoryapi.listener.event.container;

import org.bukkit.entity.Player;

import ru.soknight.packetinventoryapi.container.type.MerchantContainer;
import ru.soknight.packetinventoryapi.event.container.TradeSelectEvent;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface TradeSelectListener extends EventListener<TradeSelectEvent> {

    @Override
    default void handle(TradeSelectEvent event) {
        handle(event.getActor(), event.getContainer(), event.getSelectedSlot());
    }
    
    void handle(Player actor, MerchantContainer container, int selectedSlot);
    
}
