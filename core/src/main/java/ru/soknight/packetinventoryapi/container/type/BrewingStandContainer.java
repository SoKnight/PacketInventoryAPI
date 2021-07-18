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

public class BrewingStandContainer extends Container<BrewingStandContainer, BrewingStandContainer.BrewingStandUpdateRequest> {

    public static final int LEFT_BOTTLE_SLOT = 0;
    public static final int CENTER_BOTTLE_SLOT = 1;
    public static final int RIGHT_BOTTLE_SLOT = 2;
    public static final int POTION_INGREDIENT_SLOT = 3;
    public static final int BLAZE_POWDER_SLOT = 4;

    private BrewingStandContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.BREWING_STAND, title);
    }

    private BrewingStandContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.BREWING_STAND, title);
    }

    public static BrewingStandContainer create(Player inventoryHolder, String title) {
        return new BrewingStandContainer(inventoryHolder, title);
    }

    public static BrewingStandContainer create(Player inventoryHolder, BaseComponent title) {
        return new BrewingStandContainer(inventoryHolder, title);
    }

    public static BrewingStandContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.BREWING_STAND);
    }

    @Override
    protected BrewingStandContainer getThis() {
        return this;
    }

    @Override
    public BrewingStandContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public BrewingStandUpdateRequest updateContent() {
        return BrewingStandUpdateRequest.create(this, contentData);
    }

    /**************************
     *  Container properties  *
     *************************/
    
    public BrewingStandContainer updateBrewTime(int value) {
        updateProperty(Property.BrewingStand.BREW_TIME, value);
        return this;
    }
    
    public BrewingStandContainer updateFuelTime(int value) {
        updateProperty(Property.BrewingStand.FUEL_TIME, value);
        return this;
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, 4);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(5, 31);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(32, 40);
    }

    public interface BrewingStandUpdateRequest extends ContentUpdateRequest<BrewingStandContainer, BrewingStandUpdateRequest> {

        static BrewingStandUpdateRequest create(BrewingStandContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseBrewingStandUpdateRequest(container, contentData);
        }

        static BrewingStandUpdateRequest create(BrewingStandContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseBrewingStandUpdateRequest(container, contentData, slotsOffset);
        }

        BrewingStandUpdateRequest leftBottle(ItemStack item);
        default BrewingStandUpdateRequest leftBottle(Material type, int amount) { return leftBottle(new ItemStack(type, amount)); }
        default BrewingStandUpdateRequest leftBottle(Material type) { return leftBottle(type, 1); }

        BrewingStandUpdateRequest centerBottle(ItemStack item);
        default BrewingStandUpdateRequest centerBottle(Material type, int amount) { return centerBottle(new ItemStack(type, amount)); }
        default BrewingStandUpdateRequest centerBottle(Material type) { return centerBottle(type, 1); }

        BrewingStandUpdateRequest rightBottle(ItemStack item);
        default BrewingStandUpdateRequest rightBottle(Material type, int amount) { return rightBottle(new ItemStack(type, amount)); }
        default BrewingStandUpdateRequest rightBottle(Material type) { return rightBottle(type, 1); }

        BrewingStandUpdateRequest potionIngredient(ItemStack item);
        default BrewingStandUpdateRequest potionIngredient(Material type, int amount) { return potionIngredient(new ItemStack(type, amount)); }
        default BrewingStandUpdateRequest potionIngredient(Material type) { return potionIngredient(type, 1); }

        BrewingStandUpdateRequest blazePowder(ItemStack item);
        default BrewingStandUpdateRequest blazePowder(Material type, int amount) { return blazePowder(new ItemStack(type, amount)); }
        default BrewingStandUpdateRequest blazePowder(Material type) { return blazePowder(type, 1); }

    }

    private static final class BaseBrewingStandUpdateRequest extends BaseContentUpdateRequest<BrewingStandContainer, BrewingStandUpdateRequest> implements BrewingStandUpdateRequest {
        private BaseBrewingStandUpdateRequest(BrewingStandContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseBrewingStandUpdateRequest(BrewingStandContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public BrewingStandUpdateRequest leftBottle(ItemStack item) {
            return set(item, LEFT_BOTTLE_SLOT, true);
        }

        @Override
        public BrewingStandUpdateRequest centerBottle(ItemStack item) {
            return set(item, CENTER_BOTTLE_SLOT, true);
        }

        @Override
        public BrewingStandUpdateRequest rightBottle(ItemStack item) {
            return set(item, RIGHT_BOTTLE_SLOT, true);
        }

        @Override
        public BrewingStandUpdateRequest potionIngredient(ItemStack item) {
            return set(item, POTION_INGREDIENT_SLOT, true);
        }

        @Override
        public BrewingStandUpdateRequest blazePowder(ItemStack item) {
            return set(item, BLAZE_POWDER_SLOT, true);
        }
    }
    
}
