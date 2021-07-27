package ru.soknight.packetinventoryapi.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

public interface PlaceholderReplacer {

    PlaceholderReplacer DEFAULT = new PlaceholderReplacerStub();
    PlaceholderReplacer PLAYER_NAME = (LitePlaceholderReplacer) (player, c) -> c.replace("%player_name%", player.getName());

    void replace(@NotNull Player player, @NotNull StringContainer container);

    void replace(@NotNull Player player, @NotNull ListContainer container);

}
