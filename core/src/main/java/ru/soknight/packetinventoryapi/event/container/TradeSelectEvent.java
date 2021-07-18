package ru.soknight.packetinventoryapi.event.container;

import org.bukkit.entity.Player;

import lombok.Getter;
import ru.soknight.packetinventoryapi.container.type.MerchantContainer;
import ru.soknight.packetinventoryapi.event.Event;

@Getter
public class TradeSelectEvent extends Event<MerchantContainer, MerchantContainer.MerchantContentUpdateRequest> {

    private final int selectedSlot;

    public TradeSelectEvent(Player actor, int selectedSlot) {
        this(actor, null, selectedSlot);
    }

    public TradeSelectEvent(Player player, MerchantContainer container, int selectedSlot) {
        super(player, container);
        this.selectedSlot = selectedSlot;
    }

    @Override
    public String toString() {
        return "TradeSelectEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", selectedSlot=" + selectedSlot +
                '}';
    }

}
