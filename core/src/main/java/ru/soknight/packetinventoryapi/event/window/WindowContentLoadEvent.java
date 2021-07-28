package ru.soknight.packetinventoryapi.event.window;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;

@Getter
public class WindowContentLoadEvent<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends Event<C, R> {

    public WindowContentLoadEvent(Player actor) {
        this(actor, null);
    }

    public WindowContentLoadEvent(Player actor, C container) {
        super(actor, container);
    }

    @Override
    public String toString() {
        return "WindowContentLoadEvent{" +
                "actor=" + actor +
                ", container=" + container +
                '}';
    }

}
