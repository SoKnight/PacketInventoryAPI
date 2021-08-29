package ru.soknight.packetinventoryapi.item.update.content.filler.row;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class AbstractRowFiller implements RowFiller {

    protected static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    protected AbstractRowFiller instance;
    protected boolean forceReplace;

    protected ItemStack itemStack;
    protected DisplayableMenuItem menuItem;

    protected int[] rows;

    public AbstractRowFiller() {
        this.instance = this;
        this.forceReplace = false;
    }

    @Override public boolean usesForceReplace() { return forceReplace; }

    @Override public @Nullable ItemStack getItemStack() { return itemStack; }
    @Override public boolean hasItemStack() { return itemStack != null; }

    @Override public @Nullable DisplayableMenuItem getMenuItem() { return menuItem; }
    @Override public boolean hasMenuItem() { return menuItem != null; }

    @Override public int[] getRows() { return rows; }
    @Override public boolean hasRows() { return rows != null && rows.length != 0; }

    public abstract class AbstractRowBuilder implements Builder {

        @Override
        public @NotNull RowFiller build() {
            return instance;
        }

        @Override
        public Builder useForceReplace(boolean enabled) {
            instance.forceReplace = enabled;
            return this;
        }

        @Override
        public Builder useForceReplace() {
            return useForceReplace(true);
        }

        @Override
        public Builder withEmptyItem() {
            return withItemStack(EMPTY_ITEM);
        }

        @Override
        public Builder withItemStack(@NotNull ItemStack itemStack) {
            instance.itemStack = itemStack;
            return this;
        }

        @Override
        public Builder withItemStack(@NotNull Material itemType) {
            Validate.notNull(itemType, "itemType");
            return withItemStack(itemType, 1);
        }

        @Override
        public Builder withItemStack(@NotNull Material itemType, int amount) {
            Validate.notNull(itemType, "itemType");
            Validate.isTrue(amount > 0, "'amount' must be more than 0!");
            return withItemStack(new ItemStack(itemType, amount));
        }

        @Override
        public Builder withMenuItem(@NotNull DisplayableMenuItem menuItem) {
            Validate.notNull(menuItem, "menuItem");
            instance.menuItem = menuItem;
            return this;
        }

        @Override
        public Builder withRow(int row) {
            Validate.isTrue(row >= 0, "'row' must be not negative number");
            instance.rows = new int[]{row};
            return this;
        }

        @Override
        public Builder withRows(int... rows) {
            instance.rows = rows;
            return this;
        }

        @Override
        public Builder withRows(Iterable<Integer> rows) {
            Validate.notNull(rows, "rows");

            List<Integer> list = new ArrayList<>();
            rows.forEach(list::add);

            instance.rows = list.stream()
                    .mapToInt(Integer::intValue)
                    .filter(row -> row >= 0)
                    .toArray();

            return this;
        }

        @Override
        public Builder withRowsRange(@NotNull IntRange rowsRange) {
            Validate.notNull(rowsRange, "rowsRange");
            return withRowsRange(rowsRange.getMin(), rowsRange.getMax());
        }

        @Override
        public Builder withRowsRange(int sourceRow, int destRow) {
            Validate.isTrue(sourceRow >= 0 && destRow >= 0, "rows must be not negative numbers");
            Validate.isTrue(sourceRow <= destRow, "'sourceRow' must be less than or equals 'destRow'");

            instance.rows = IntStream.rangeClosed(sourceRow, destRow).toArray();
            return this;
        }

    }

}
