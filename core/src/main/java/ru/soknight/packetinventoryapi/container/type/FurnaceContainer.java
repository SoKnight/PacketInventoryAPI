package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.item.update.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

public class FurnaceContainer<F extends FurnaceContainer<F>> extends Container<F, FurnaceContainer.FurnaceUpdateRequest<F>> {

    public static final int INGREDIENT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    private FurnaceContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.FURNACE, title);
    }

    private FurnaceContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.FURNACE, title);
    }
    
    protected FurnaceContainer(Player inventoryHolder, ContainerType inventoryType, String title) {
        super(inventoryHolder, inventoryType, title);
    }

    protected FurnaceContainer(Player inventoryHolder, ContainerType inventoryType, BaseComponent title) {
        super(inventoryHolder, inventoryType, title);
    }

    public static FurnaceContainer<?> create(Player inventoryHolder, String title) {
        return new DefaultFurnace(inventoryHolder, title);
    }

    public static FurnaceContainer<?> create(Player inventoryHolder, BaseComponent title) {
        return new DefaultFurnace(inventoryHolder, title);
    }

    public static FurnaceContainer<?> createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.FURNACE);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected F getThis() {
        return (F) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public F copy(Player holder) {
        return (F) create(holder, title.duplicate());
    }

    @Override
    public FurnaceUpdateRequest<F> updateContent() {
        return FurnaceUpdateRequest.create(getThis(), contentData);
    }

    /**************************
     *  Container properties  *
     *************************/
    
    public F updateFuelBurnTimeLeft(int value) {
        updateProperty(Property.Furnace.FUEL_BURN_TIME_LEFT, value);
        return getThis();
    }
    
    public F updateMaxFuelBurnTime(int value) {
        updateProperty(Property.Furnace.MAX_FUEL_BURN_TIME, value);
        return getThis();
    }
    
    public F updateCurrentProgress(int value) {
        updateProperty(Property.Furnace.CURRENT_PROGRESS, value);
        return getThis();
    }
    
    public F updateMaxProgress(int value) {
        updateProperty(Property.Furnace.MAX_PROGRESS, value);
        return getThis();
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, 2);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(3, 29);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(30, 38);
    }

    public static final class DefaultFurnace extends FurnaceContainer<DefaultFurnace> {
        private DefaultFurnace(Player inventoryHolder, String title) {
            super(inventoryHolder, ContainerType.FURNACE, title);
        }

        private DefaultFurnace(Player inventoryHolder, BaseComponent title) {
            super(inventoryHolder, ContainerType.FURNACE, title);
        }

        @Override
        protected DefaultFurnace getThis() {
            return this;
        }
    }

    public interface FurnaceUpdateRequest<F extends FurnaceContainer<F>> extends ContentUpdateRequest<F, FurnaceUpdateRequest<F>> {

        static <F extends FurnaceContainer<F>> FurnaceUpdateRequest<F> create(F container, Map<Integer, ItemStack> contentData) {
            return new BaseFurnaceUpdateRequest<>(container, contentData);
        }

        static <F extends FurnaceContainer<F>> FurnaceUpdateRequest<F> create(F container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseFurnaceUpdateRequest<>(container, contentData, slotsOffset);
        }

        FurnaceUpdateRequest<F> ingredient(ItemStack item);
        default FurnaceUpdateRequest<F> ingredient(Material type, int amount) { return ingredient(new ItemStack(type, amount)); }
        default FurnaceUpdateRequest<F> ingredient(Material type) { return ingredient(type, 1); }

        FurnaceUpdateRequest<F> fuel(ItemStack item);
        default FurnaceUpdateRequest<F> fuel(Material type, int amount) { return fuel(new ItemStack(type, amount)); }
        default FurnaceUpdateRequest<F> fuel(Material type) { return fuel(type, 1); }

        FurnaceUpdateRequest<F> outputItem(ItemStack item);
        default FurnaceUpdateRequest<F> outputItem(Material type, int amount) { return outputItem(new ItemStack(type, amount)); }
        default FurnaceUpdateRequest<F> outputItem(Material type) { return outputItem(type, 1); }

    }

    private static final class BaseFurnaceUpdateRequest<F extends FurnaceContainer<F>> extends BaseContentUpdateRequest<F, FurnaceUpdateRequest<F>> implements FurnaceUpdateRequest<F> {
        private BaseFurnaceUpdateRequest(F container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseFurnaceUpdateRequest(F container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public FurnaceUpdateRequest<F> ingredient(ItemStack item) {
            return set(item, INGREDIENT_SLOT, true);
        }

        @Override
        public FurnaceUpdateRequest<F> fuel(ItemStack item) {
            return set(item, FUEL_SLOT, true);
        }

        @Override
        public FurnaceUpdateRequest<F> outputItem(ItemStack item) {
            return set(item, OUTPUT_SLOT, true);
        }
    }
    
}
