package ru.soknight.packetinventoryapi.event.container;

import org.bukkit.entity.Player;

import lombok.Getter;
import ru.soknight.packetinventoryapi.container.type.AnvilContainer;
import ru.soknight.packetinventoryapi.event.Event;

@Getter
public class AnvilRenameEvent extends Event<AnvilContainer, AnvilContainer.AnvilUpdateRequest> {

    private final String customName;

    public AnvilRenameEvent(Player actor, String customName) {
        this(actor, null, customName);
    }

    public AnvilRenameEvent(Player actor, AnvilContainer container, String customName) {
        super(actor, container);
        this.customName = customName;
    }

    @Override
    public String toString() {
        return "AnvilRenameEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", customName='" + customName + '\'' +
                '}';
    }

}
