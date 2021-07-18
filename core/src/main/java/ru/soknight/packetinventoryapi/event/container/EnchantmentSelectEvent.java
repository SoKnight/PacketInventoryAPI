package ru.soknight.packetinventoryapi.event.container;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.data.EnchantmentPosition;
import ru.soknight.packetinventoryapi.container.type.EnchantmentTableContainer;
import ru.soknight.packetinventoryapi.event.Event;

@Getter
public class EnchantmentSelectEvent extends Event<EnchantmentTableContainer, EnchantmentTableContainer.EnchantmentTableUpdateRequest> {

    private final EnchantmentPosition enchantmentPosition;

    public EnchantmentSelectEvent(Player actor, EnchantmentPosition enchantmentPosition) {
        this(actor, null, enchantmentPosition);
    }

    public EnchantmentSelectEvent(
            Player actor,
            EnchantmentTableContainer container,
            EnchantmentPosition position
    ) {
        super(actor, container);
        this.enchantmentPosition = position;
    }

    @Override
    public String toString() {
        return "EnchantmentSelectEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", enchantmentPosition=" + enchantmentPosition +
                '}';
    }

}
