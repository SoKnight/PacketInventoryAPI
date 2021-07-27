package ru.soknight.packetinventoryapi.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

final class PlaceholderReplacerStub implements PlaceholderReplacer {

    @Override
    public void replace(@NotNull Player player, @NotNull StringContainer original) {}

    @Override
    public void replace(@NotNull Player player, @NotNull ListContainer original) {}

}
