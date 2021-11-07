package ru.soknight.packetinventoryapi.event.window;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;

@Getter
public class WindowPostCloseEvent<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends Event<C, R> {

    private final boolean requestedByHolder;
    private final boolean closedActually;

    public WindowPostCloseEvent(Player actor, boolean requestedByHolder, boolean closedActually) {
        this(actor, null, requestedByHolder, closedActually);
    }

    public WindowPostCloseEvent(Player actor, C container, boolean requestedByHolder, boolean closedActually) {
        super(actor, container);
        this.requestedByHolder = requestedByHolder;
        this.closedActually = closedActually;
    }

    @Override
    public String toString() {
        return "WindowPostCloseEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", requestedByHolder=" + requestedByHolder +
                ", closedActually=" + closedActually +
                '}';
    }

}
