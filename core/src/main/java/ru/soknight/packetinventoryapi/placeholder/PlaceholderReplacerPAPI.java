package ru.soknight.packetinventoryapi.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

import java.util.List;

public class PlaceholderReplacerPAPI implements PlaceholderReplacer {

    @Override
    public void replace(@NotNull Player player, @NotNull StringContainer container) {
        String replaced = PlaceholderAPI.setPlaceholders(player, container.getString());
        container.setString(replaced);
    }

    @Override
    public void replace(@NotNull Player player, @NotNull ListContainer container) {
        List<String> replaced = PlaceholderAPI.setPlaceholders(player, container.getList());
        container.loadContentFrom(replaced);
    }

}
