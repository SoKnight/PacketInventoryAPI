package ru.soknight.packetinventoryapi.menu.container;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;

public interface CloneableContainer<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    C copyFull(Player holder, AnyEventListener listener);

}
