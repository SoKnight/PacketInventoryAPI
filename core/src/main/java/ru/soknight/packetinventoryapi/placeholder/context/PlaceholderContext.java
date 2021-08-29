package ru.soknight.packetinventoryapi.placeholder.context;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.menu.context.Context;
import ru.soknight.packetinventoryapi.placeholder.LitePlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

import java.util.List;

public interface PlaceholderContext extends Context {

    static @NotNull PlaceholderContext create(@Nullable Player viewer) {
        return new SimplePlaceholderContext(viewer);
    }

    @NotNull PlaceholderContext duplicateFor(@Nullable Player viewer);

    @NotNull PlaceholderContext appendReplacerFirst(@NotNull PlaceholderReplacer replacer);
    @NotNull PlaceholderContext appendReplacer(@NotNull PlaceholderReplacer replacer);

    @NotNull PlaceholderContext appendReplacerFirst(@NotNull LitePlaceholderReplacer replacer);
    @NotNull PlaceholderContext appendReplacer(@NotNull LitePlaceholderReplacer replacer);

    @NotNull PlaceholderContext appendPAPIReplacerFirst();
    @NotNull PlaceholderContext appendPAPIReplacer();

    @NotNull @UnmodifiableView List<PlaceholderReplacer> getReplacers();

    @NotNull PlaceholderContext removeReplacer(@NotNull PlaceholderReplacer replacer);

    @NotNull String replacePlaceholders(@NotNull String original, @Nullable Integer slot);
    @NotNull PlaceholderContext replacePlaceholders(@NotNull StringContainer container);

    @NotNull List<String> replacePlaceholders(@NotNull List<String> original, @Nullable Integer slot);
    @NotNull PlaceholderContext replacePlaceholders(@NotNull ListContainer container);

    @NotNull ItemStack replacePlaceholders(@NotNull ItemStack original, @Nullable Integer slot);

}
