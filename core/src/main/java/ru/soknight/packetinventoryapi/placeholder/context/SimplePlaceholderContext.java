package ru.soknight.packetinventoryapi.placeholder.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacer;

import java.util.List;

public final class SimplePlaceholderContext extends AbstractPlaceholderContext {

    SimplePlaceholderContext(@NotNull Player viewer) {
        super(viewer);
    }

    private SimplePlaceholderContext(@NotNull Player viewer, @NotNull List<PlaceholderReplacer> placeholderReplacers) {
        super(viewer, placeholderReplacers);
    }

    @Override
    public @NotNull PlaceholderContext duplicateFor(Player viewer) {
        return new SimplePlaceholderContext(viewer, placeholderReplacers);
    }

}
