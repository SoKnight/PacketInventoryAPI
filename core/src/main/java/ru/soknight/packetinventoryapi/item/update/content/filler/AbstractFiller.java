package ru.soknight.packetinventoryapi.item.update.content.filler;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Arrays;
import java.util.OptionalInt;

public abstract class AbstractFiller implements Filler {

    protected static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    protected AbstractFiller instance;
    protected boolean forceReplace;

    protected ItemStack itemStack;
    protected DisplayableMenuItem menuItem;

    protected int[] slots;

    protected Integer sourceSlot;
    protected Integer destSlot;

    protected Integer fromX;
    protected Integer fromY;
    protected Integer toX;
    protected Integer toY;

    protected FillPatternType fillPattern;

    public AbstractFiller() {
        this.instance = this;
        this.forceReplace = false;
    }

    @Override public boolean usesForceReplace() { return forceReplace; }

    @Override public @Nullable ItemStack getItemStack() { return itemStack; }
    @Override public boolean hasItemStack() { return itemStack != null; }

    @Override public @Nullable DisplayableMenuItem getMenuItem() { return menuItem; }
    @Override public boolean hasMenuItem() { return menuItem != null; }

    @Override public @Nullable int[] getSlots() { return slots; }
    @Override public boolean hasSlots() { return slots != null && slots.length != 0; }

    @Override public OptionalInt getSourceSlot() { return wrapInt(sourceSlot); }
    @Override public OptionalInt getDestSlot() { return wrapInt(destSlot); }
    @Override public boolean hasSlotRange() { return allNonNull(sourceSlot, destSlot); }

    @Override public OptionalInt getFromX() { return wrapInt(fromX); }
    @Override public OptionalInt getFromY() { return wrapInt(fromY); }
    @Override public OptionalInt getToX() { return wrapInt(toX); }
    @Override public OptionalInt getToY() { return wrapInt(toY); }
    @Override public boolean hasSlotRegion() { return allNonNull(fromX, fromY, toX, toY);}

    @Override public @Nullable FillPatternType getFillPattern() { return fillPattern; }
    @Override public boolean hasFillPattern() { return fillPattern != null; }

    public abstract class AbstractBuilder implements Filler.Builder {

        @Override
        public @NotNull Filler build() {
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

            int[] slots = menuItem.getSlots();
            if(slots != null)
                instance.slots = slots;

            FillPatternType fillPattern = menuItem.getFillPattern();
            if(fillPattern != null)
                instance.fillPattern = fillPattern;

            return this;
        }

        @Override
        public Builder withSlotRange(@NotNull IntRange slotRange) {
            Validate.notNull(slotRange, "slotRange");
            return withSlotRange(slotRange.getMin(), slotRange.getMax());
        }

        @Override
        public Builder withSlotRange(int sourceSlot, int destSlot) {
            Validate.isTrue(sourceSlot >= 0 && destSlot >= 0, "slots must be not negative numbers");
            Validate.isTrue(sourceSlot <= destSlot, "'sourceSlot' must be less than or equals 'destSlot'");

            instance.sourceSlot = sourceSlot;
            instance.destSlot = destSlot;

            return this;
        }

        @Override
        public Builder withSlotRegion(@NotNull IntRange rangeX, @NotNull IntRange rangeY) {
            Validate.notNull(rangeX, "rangeX");
            Validate.notNull(rangeY, "rangeY");
            return withSlotRegion(rangeX.getMin(), rangeY.getMin(), rangeX.getMax(), rangeY.getMax());
        }

        @Override
        public Builder withSlotRegion(int fromX, int fromY, int toX, int toY) {
            Validate.isTrue(fromX >= 0 && fromX <= 8, "'fromX' must be in the range [0;8]");
            Validate.isTrue(toX >= 0 && toX <= 8, "'fromX' must be in the range [0;8]");
            Validate.isTrue(fromX <= toX, "'fromX' must be less than or equals 'toX'");

            Validate.isTrue(fromY >= 0, "'fromY' must be not negative number");
            Validate.isTrue(toY >= 0, "'toY' must be not negative number");
            Validate.isTrue(fromY <= toY, "'fromY' must be less than or equals 'toY'");

            instance.fromX = fromX;
            instance.fromY = fromY;
            instance.toX = toX;
            instance.toY = toY;

            return this;
        }

        @Override
        public Builder withFillPattern(@NotNull FillPatternType fillPattern) {
            Validate.notNull(fillPattern, "fillPattern");
            instance.fillPattern = fillPattern;
            return this;
        }

    }

    protected OptionalInt wrapInt(Integer value) {
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    protected boolean allNonNull(Object... objects) {
        if(objects == null || objects.length == 0)
            return true;

        for(Object object : objects)
            if(object == null)
                return false;

        return true;
    }

    @Override
    public String toString() {
        return "Filler{" +
                "forceReplace=" + forceReplace +
                ", itemStack=" + itemStack +
                ", menuItem=" + menuItem +
                ", slots=" + Arrays.toString(slots) +
                ", sourceSlot=" + sourceSlot +
                ", destSlot=" + destSlot +
                ", fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                ", fillPattern=" + fillPattern +
                '}';
    }

}
