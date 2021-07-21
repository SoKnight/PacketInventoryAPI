package ru.soknight.packetinventoryapi.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlaceholderReplacerPAPI implements PlaceholderReplacer {

    @Override
    public @NotNull String replace(@NotNull Player player, @NotNull String original) {
        return PlaceholderAPI.setPlaceholders(player, original);
    }

    @Override
    public @NotNull List<String> replace(@NotNull Player player, @NotNull List<String> original) {
        return PlaceholderAPI.setPlaceholders(player, original);
    }

}
