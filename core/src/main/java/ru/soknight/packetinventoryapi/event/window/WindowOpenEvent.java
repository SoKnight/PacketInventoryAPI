package ru.soknight.packetinventoryapi.event.window;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;

@Getter
public class WindowOpenEvent<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends Event<C, R> {

    private final boolean reopened;

    public WindowOpenEvent(Player actor, boolean reopened) {
        this(actor, null, reopened);
    }

    public WindowOpenEvent(Player actor, C container, boolean reopened) {
        super(actor, container);
        this.reopened = reopened;
    }

    @Override
    public String toString() {
        return "WindowOpenEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", reopened=" + reopened +
                '}';
    }

}
