package ru.soknight.packetinventoryapi.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Event<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    protected final Player actor;
    protected C container;

    public ContainerType getContainerType() {
        return container != null ? container.getContainerType() : null;
    }

    @SuppressWarnings("unchecked")
    public void setContainer(Container<?, ?> container) {
        this.container = (C) container;
    }

}
