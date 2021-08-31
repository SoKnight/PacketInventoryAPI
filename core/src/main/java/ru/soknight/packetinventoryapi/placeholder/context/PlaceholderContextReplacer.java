package ru.soknight.packetinventoryapi.placeholder.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

public final class PlaceholderContextReplacer implements PlaceholderReplacer {

    private final PlaceholderContext context;

    PlaceholderContextReplacer(@NotNull PlaceholderContext context) {
        this.context = context;
    }

    @Override
    public void replace(@NotNull Player player, @NotNull StringContainer container) {
        context.getReplacers().forEach(replacer -> replacer.replace(player, container));
    }

    @Override
    public void replace(@NotNull Player player, @NotNull ListContainer container) {
        context.getReplacers().forEach(replacer -> replacer.replace(player, container));
    }

}
