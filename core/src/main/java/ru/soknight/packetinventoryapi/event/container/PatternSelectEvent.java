package ru.soknight.packetinventoryapi.event.container;

import org.bukkit.entity.Player;

import lombok.Getter;
import ru.soknight.packetinventoryapi.container.type.LoomContainer;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.util.MathFunction;

@Getter
public class PatternSelectEvent extends Event<LoomContainer, LoomContainer.LoomUpdateRequest> {

    private static final MathFunction COLUMN_FINDER = i -> i % 4;
    private static final MathFunction ROW_FINDER = i -> i / 4;
    
    private final int slot;

    public PatternSelectEvent(Player actor, int slot) {
        this(actor, null, slot);
    }

    public PatternSelectEvent(Player actor, LoomContainer container, int slot) {
        super(actor, container);
        this.slot = slot;
    }
    
    public int getColumn() {
        return COLUMN_FINDER.process(slot);
    }
    
    public int getRow() {
        return ROW_FINDER.process(slot);
    }

    @Override
    public String toString() {
        return "PatternSelectEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", slot=" + slot +
                '}';
    }

}
