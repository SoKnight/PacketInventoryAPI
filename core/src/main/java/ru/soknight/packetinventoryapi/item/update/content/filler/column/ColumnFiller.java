package ru.soknight.packetinventoryapi.item.update.content.filler.column;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.util.IntRange;

public interface ColumnFiller {

    static @NotNull ColumnFiller.Builder builder() {
        return SimpleColumnFiller.builder();
    }

    boolean usesForceReplace();

    @Nullable ItemStack getItemStack();
    boolean hasItemStack();

    @Nullable DisplayableMenuItem getMenuItem();
    boolean hasMenuItem();

    @Nullable int[] getColumns();
    boolean hasColumns();

    interface Builder {

        static @NotNull Builder create() {
            return builder();
        }

        @NotNull ColumnFiller build();

        // --- 'force replace' flag
        Builder useForceReplace(boolean enabled);

        Builder useForceReplace();

        // --- item
        Builder withEmptyItem();

        Builder withItemStack(@NotNull ItemStack itemStack);

        Builder withItemStack(@NotNull Material itemType);

        Builder withItemStack(@NotNull Material itemType, int amount);

        Builder withMenuItem(@NotNull DisplayableMenuItem menuItem);

        // --- columns
        Builder withColumn(int column);

        Builder withColumns(int... columns);

        Builder withColumns(@NotNull Iterable<Integer> columns);

        Builder withColumnsRange(@NotNull IntRange columnsRange);

        Builder withColumnsRange(int sourceColumn, int destColumn);

    }
    
}
