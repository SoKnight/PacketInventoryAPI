package ru.soknight.packetinventoryapi.item.update.content.filler.row;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.util.IntRange;

public interface RowFiller {

    static @NotNull RowFiller.Builder builder() {
        return SimpleRowFiller.builder();
    }

    boolean usesForceReplace();

    @Nullable ItemStack getItemStack();
    boolean hasItemStack();

    @Nullable DisplayableMenuItem getMenuItem();
    boolean hasMenuItem();

    @Nullable int[] getRows();
    boolean hasRows();

    interface Builder {

        static @NotNull Builder create() {
            return builder();
        }

        @NotNull RowFiller build();

        // --- 'force replace' flag
        Builder useForceReplace(boolean enabled);

        Builder useForceReplace();

        // --- item
        Builder withEmptyItem();

        Builder withItemStack(@NotNull ItemStack itemStack);

        Builder withItemStack(@NotNull Material itemType);

        Builder withItemStack(@NotNull Material itemType, int amount);

        Builder withMenuItem(@NotNull DisplayableMenuItem menuItem);

        // --- rows
        Builder withRow(int row);

        Builder withRows(int... rows);

        Builder withRows(@NotNull Iterable<Integer> rows);

        Builder withRowsRange(@NotNull IntRange rowsRange);

        Builder withRowsRange(int sourceRow, int destRow);

    }
    
}
