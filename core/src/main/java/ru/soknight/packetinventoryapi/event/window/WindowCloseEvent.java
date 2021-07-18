package ru.soknight.packetinventoryapi.event.window;

import lombok.Getter;
import org.bukkit.entity.Player;

import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;

@Getter
public class WindowCloseEvent<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends Event<C, R> {

    private final boolean closedByHolder;

    public WindowCloseEvent(Player actor, boolean closedByHolder) {
        this(actor, null, closedByHolder);
    }

    public WindowCloseEvent(Player actor, C container, boolean closedByHolder) {
        super(actor, container);
        this.closedByHolder = closedByHolder;
    }

    @Override
    public String toString() {
        return "WindowCloseEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", closedByHolder=" + closedByHolder +
                '}';
    }

}
