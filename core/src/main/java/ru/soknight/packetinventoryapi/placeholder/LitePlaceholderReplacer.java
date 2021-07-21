package ru.soknight.packetinventoryapi.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface LitePlaceholderReplacer extends PlaceholderReplacer {

    @Override
    default @NotNull List<String> replace(@NotNull Player player, @NotNull List<String> original) {
        original.replaceAll(line -> replace(player, line));
        return original;
    }

}
