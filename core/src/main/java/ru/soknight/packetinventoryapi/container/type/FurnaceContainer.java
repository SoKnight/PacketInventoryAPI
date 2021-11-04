package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.ContainerTypes;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

public abstract class FurnaceContainer<F extends FurnaceContainer<F>> extends Container<F, FurnaceContainer.FurnaceUpdateRequest<F>> {

    public static final int INGREDIENT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    private FurnaceContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.FURNACE, title);
    }

    private FurnaceContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.FURNACE, title);
    }
    
    protected FurnaceContainer(@Nullable Player inventoryHolder, @NotNull ContainerType inventoryType, @Nullable String title) {
        super(inventoryHolder, inventoryType, title);
    }

    protected FurnaceContainer(@Nullable Player inventoryHolder, @NotNull ContainerType inventoryType, @NotNull BaseComponent title) {
        super(inventoryHolder, inventoryType, title);
    }

    public static @NotNull FurnaceContainer<?> create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new DefaultFurnace(inventoryHolder, title);
    }

    public static @NotNull FurnaceContainer<?> create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new DefaultFurnace(inventoryHolder, title);
    }

    public static @NotNull FurnaceContainer<?> createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.FURNACE);
    }

    @Override
    protected abstract @NotNull F getThis();

    @Override
    public @NotNull FurnaceUpdateRequest<F> updateContent() {
        return FurnaceUpdateRequest.create(getThis(), contentData);
    }

    /**************************
     *  Container properties  *
     *************************/
    
    public @NotNull F updateFuelBurnTimeLeft(int value) {
        updateProperty(Property.Furnace.FUEL_BURN_TIME_LEFT, value);
        return getThis();
    }
    
    public @NotNull F updateMaxFuelBurnTime(int value) {
        updateProperty(Property.Furnace.MAX_FUEL_BURN_TIME, value);
        return getThis();
    }
    
    public @NotNull F updateCurrentProgress(int value) {
        updateProperty(Property.Furnace.CURRENT_PROGRESS, value);
        return getThis();
    }
    
    public @NotNull F updateMaxProgress(int value) {
        updateProperty(Property.Furnace.MAX_PROGRESS, value);
        return getThis();
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, 2);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(3, 29);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(30, 38);
    }

    public static final class DefaultFurnace extends FurnaceContainer<DefaultFurnace> {
        private DefaultFurnace(@Nullable Player inventoryHolder, @Nullable String title) {
            super(inventoryHolder, ContainerTypes.FURNACE, title);
        }

        private DefaultFurnace(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
            super(inventoryHolder, ContainerTypes.FURNACE, title);
        }

        @Override
        protected @NotNull DefaultFurnace getThis() {
            return this;
        }

        @Override
        public @NotNull DefaultFurnace copy(@Nullable Player holder) {
            return new DefaultFurnace(holder, title.duplicate());
        }
    }

    public interface FurnaceUpdateRequest<F extends FurnaceContainer<F>> extends ContentUpdateRequest<F, FurnaceUpdateRequest<F>> {

        static <F extends FurnaceContainer<F>> @NotNull FurnaceUpdateRequest<F> create(
                @NotNull F container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseFurnaceUpdateRequest<>(container, contentData);
        }

        static <F extends FurnaceContainer<F>> @NotNull FurnaceUpdateRequest<F> create(
                @NotNull F container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseFurnaceUpdateRequest<>(container, contentData, slotsOffset);
        }

        // --- ingredient slot
        @NotNull FurnaceUpdateRequest<F> ingredient(@Nullable ItemStack item);

        default @NotNull FurnaceUpdateRequest<F> ingredient(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return ingredient(new ItemStack(type, amount));
        }

        default @NotNull FurnaceUpdateRequest<F> ingredient(@NotNull Material type) {
            return ingredient(type, 1);
        }

        // --- fuel slot
        @NotNull FurnaceUpdateRequest<F> fuel(@Nullable ItemStack item);

        default @NotNull FurnaceUpdateRequest<F> fuel(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return fuel(new ItemStack(type, amount));
        }

        default @NotNull FurnaceUpdateRequest<F> fuel(@NotNull Material type) {
            return fuel(type, 1);
        }

        // --- output item slot
        @NotNull FurnaceUpdateRequest<F> outputItem(@Nullable ItemStack item);

        default @NotNull FurnaceUpdateRequest<F> outputItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return outputItem(new ItemStack(type, amount));
        }

        default @NotNull FurnaceUpdateRequest<F> outputItem(@NotNull Material type) {
            return outputItem(type, 1);
        }

    }

    private static final class BaseFurnaceUpdateRequest<F extends FurnaceContainer<F>> extends BaseContentUpdateRequest<F, FurnaceUpdateRequest<F>> implements FurnaceUpdateRequest<F> {
        private BaseFurnaceUpdateRequest(
                @NotNull F container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseFurnaceUpdateRequest(
                @NotNull F container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull FurnaceUpdateRequest<F> ingredient(@Nullable ItemStack item) {
            return set(item, INGREDIENT_SLOT, true);
        }

        @Override
        public @NotNull FurnaceUpdateRequest<F> fuel(@Nullable ItemStack item) {
            return set(item, FUEL_SLOT, true);
        }

        @Override
        public @NotNull FurnaceUpdateRequest<F> outputItem(@Nullable ItemStack item) {
            return set(item, OUTPUT_SLOT, true);
        }
    }
    
}
