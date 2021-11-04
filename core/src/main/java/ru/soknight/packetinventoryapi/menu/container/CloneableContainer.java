package ru.soknight.packetinventoryapi.menu.container;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;

public interface CloneableContainer<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    @NotNull C copyFull(@Nullable Player holder, @NotNull AnyEventListener listener);

}
