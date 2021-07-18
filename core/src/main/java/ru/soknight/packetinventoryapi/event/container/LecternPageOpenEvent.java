package ru.soknight.packetinventoryapi.event.container;

import org.bukkit.entity.Player;

import lombok.Getter;
import ru.soknight.packetinventoryapi.container.type.LecternContainer;
import ru.soknight.packetinventoryapi.event.Event;

@Getter
public class LecternPageOpenEvent extends Event<LecternContainer, LecternContainer.LecternUpdateRequest> {

    private final int page;

    public LecternPageOpenEvent(Player actor, int page) {
        this(actor, null, page);
    }

    public LecternPageOpenEvent(Player player, LecternContainer container, int page) {
        super(player, container);
        this.page = page;
    }

    @Override
    public String toString() {
        return "LecternPageOpenEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", page=" + page +
                '}';
    }

}
