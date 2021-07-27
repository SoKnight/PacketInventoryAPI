package ru.soknight.packetinventoryapi.placeholder.element;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

public interface ElementPlaceholderReplacer {

    void replace(@NotNull Player player, @NotNull StringContainer container, int slot, int pageIndex, int totalIndex);

    void replace(@NotNull Player player, @NotNull ListContainer container, int slot, int pageIndex, int totalIndex);

}
