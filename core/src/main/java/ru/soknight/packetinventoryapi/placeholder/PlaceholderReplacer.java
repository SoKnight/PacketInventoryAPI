package ru.soknight.packetinventoryapi.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PlaceholderReplacer {

    PlaceholderReplacer DEFAULT = new PlaceholderReplacerStub();
    LitePlaceholderReplacer PLAYER_NAME = (player, original) -> original.replace("%player_name%", player.getName());

    @NotNull String replace(@NotNull Player player, @NotNull String original);

    @NotNull List<String> replace(@NotNull Player player, @NotNull List<String> original);

}
