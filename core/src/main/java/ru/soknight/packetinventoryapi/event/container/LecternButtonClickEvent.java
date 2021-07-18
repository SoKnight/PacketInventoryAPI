package ru.soknight.packetinventoryapi.event.container;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.data.LecternButtonType;
import ru.soknight.packetinventoryapi.container.type.LecternContainer;
import ru.soknight.packetinventoryapi.event.Event;

@Getter
public class LecternButtonClickEvent extends Event<LecternContainer, LecternContainer.LecternUpdateRequest> {

    private final LecternButtonType buttonType;

    public LecternButtonClickEvent(Player actor, LecternButtonType buttonType) {
        this(actor, null, buttonType);
    }

    public LecternButtonClickEvent(Player player, LecternContainer container, LecternButtonType buttonType) {
        super(player, container);
        this.buttonType = buttonType;
    }

    @Override
    public String toString() {
        return "LecternButtonClickEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", buttonType=" + buttonType +
                '}';
    }

}
