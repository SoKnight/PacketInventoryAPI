package ru.soknight.packetinventoryapi.item.update.content.filler;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.OptionalInt;

public interface Filler {

    static @NotNull Filler.Builder builder() {
        return SimpleFiller.builder();
    }

    boolean usesForceReplace();

    @Nullable ItemStack getItemStack();
    boolean hasItemStack();

    @Nullable DisplayableMenuItem getMenuItem();
    boolean hasMenuItem();

    @Nullable int[] getSlots();
    boolean hasSlots();

    OptionalInt getSourceSlot();
    OptionalInt getDestSlot();
    boolean hasSlotRange();

    OptionalInt getFromX();
    OptionalInt getFromY();
    OptionalInt getToX();
    OptionalInt getToY();
    boolean hasSlotRegion();

    @Nullable FillPatternType getFillPattern();
    boolean hasFillPattern();

    interface Builder {

        static @NotNull Builder create() {
            return builder();
        }

        @NotNull Filler build();

        // --- 'force replace' flag
        Builder useForceReplace(boolean enabled);

        Builder useForceReplace();

        // --- item
        Builder withEmptyItem();

        Builder withItemStack(@NotNull ItemStack itemStack);

        Builder withItemStack(@NotNull Material itemType);

        Builder withItemStack(@NotNull Material itemType, int amount);

        Builder withMenuItem(@NotNull DisplayableMenuItem menuItem);

        // --- slot range
        Builder withSlotRange(@NotNull IntRange slotRange);

        Builder withSlotRange(int sourceSlot, int destSlot);

        // --- slot region
        Builder withSlotRegion(@NotNull IntRange rangeX, @NotNull IntRange rangeY);

        Builder withSlotRegion(int fromX, int fromY, int toX, int toY);

        // --- fill pattern
        Builder withFillPattern(@NotNull FillPatternType fillPattern);

    }

}
