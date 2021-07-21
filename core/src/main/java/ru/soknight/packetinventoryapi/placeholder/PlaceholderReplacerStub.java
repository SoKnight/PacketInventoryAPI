package ru.soknight.packetinventoryapi.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class PlaceholderReplacerStub implements PlaceholderReplacer {

    @Override
    public @NotNull String replace(@NotNull Player player, @NotNull String original) {
        return original;
    }

    @Override
    public @NotNull List<String> replace(@NotNull Player player, @NotNull List<String> original) {
        return original;
    }

}
