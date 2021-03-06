package ru.soknight.packetinventoryapi.event.window;

import lombok.Getter;
import org.bukkit.entity.Player;

import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;

@Getter
public class WindowCloseEvent<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends Event<C, R> {

    private final boolean requestedByHolder;

    public WindowCloseEvent(Player actor, boolean requestedByHolder) {
        this(actor, null, requestedByHolder);
    }

    public WindowCloseEvent(Player actor, C container, boolean requestedByHolder) {
        super(actor, container);
        this.requestedByHolder = requestedByHolder;
    }

    @Override
    public String toString() {
        return "WindowCloseEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", requestedByHolder=" + requestedByHolder +
                '}';
    }

}
