package ru.soknight.packetinventoryapi.item.update.content.filler.column;

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

public abstract class AbstractColumnFiller implements ColumnFiller {

    protected static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    protected AbstractColumnFiller instance;
    protected boolean forceReplace;

    protected ItemStack itemStack;
    protected DisplayableMenuItem menuItem;

    protected int[] columns;

    public AbstractColumnFiller() {
        this.instance = this;
        this.forceReplace = false;
    }

    @Override public boolean usesForceReplace() { return forceReplace; }

    @Override public @Nullable ItemStack getItemStack() { return itemStack; }
    @Override public boolean hasItemStack() { return itemStack != null; }

    @Override public @Nullable DisplayableMenuItem getMenuItem() { return menuItem; }
    @Override public boolean hasMenuItem() { return menuItem != null; }

    @Override public int[] getColumns() { return columns; }
    @Override public boolean hasColumns() { return columns != null && columns.length != 0; }

    public abstract class AbstractColumnBuilder implements Builder {

        @Override
        public @NotNull ColumnFiller build() {
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
        public Builder withColumn(int column) {
            Validate.isTrue(column >= 0 && column <= 8, "'column' must be in the range [0;8]");
            instance.columns = new int[]{column};
            return this;
        }

        @Override
        public Builder withColumns(int... columns) {
            instance.columns = columns;
            return this;
        }

        @Override
        public Builder withColumns(Iterable<Integer> columns) {
            Validate.notNull(columns, "columns");

            List<Integer> list = new ArrayList<>();
            columns.forEach(list::add);

            instance.columns = list.stream()
                    .mapToInt(Integer::intValue)
                    .filter(column -> column >= 0)
                    .toArray();

            return this;
        }

        @Override
        public Builder withColumnsRange(@NotNull IntRange columnsRange) {
            Validate.notNull(columnsRange, "columnsRange");
            return withColumnsRange(columnsRange.getMin(), columnsRange.getMax());
        }

        @Override
        public Builder withColumnsRange(int sourceColumn, int destColumn) {
            Validate.isTrue(sourceColumn >= 0 && sourceColumn <= 8, "'sourceColumn' must be in the range [0;8]");
            Validate.isTrue(destColumn >= 0 && destColumn <= 8, "'destColumn' must be in the range [0;8]");
            
            Validate.isTrue(sourceColumn >= 0 && destColumn >= 0, "columns must be not negative numbers");
            Validate.isTrue(sourceColumn <= destColumn, "'sourceColumn' must be less than or equals 'destColumn'");

            instance.columns = IntStream.rangeClosed(sourceColumn, destColumn).toArray();
            return this;
        }

    }

}
