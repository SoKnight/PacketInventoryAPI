package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerTypes;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

public class BrewingStandContainer extends Container<BrewingStandContainer, BrewingStandContainer.BrewingStandUpdateRequest> {

    public static final int LEFT_BOTTLE_SLOT = 0;
    public static final int CENTER_BOTTLE_SLOT = 1;
    public static final int RIGHT_BOTTLE_SLOT = 2;
    public static final int POTION_INGREDIENT_SLOT = 3;
    public static final int BLAZE_POWDER_SLOT = 4;

    private BrewingStandContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.BREWING_STAND, title);
    }

    private BrewingStandContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.BREWING_STAND, title);
    }

    public static @NotNull BrewingStandContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new BrewingStandContainer(inventoryHolder, title);
    }

    public static @NotNull BrewingStandContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new BrewingStandContainer(inventoryHolder, title);
    }

    public static @NotNull BrewingStandContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.BREWING_STAND);
    }

    @Override
    protected @NotNull BrewingStandContainer getThis() {
        return this;
    }

    @Override
    public @NotNull BrewingStandContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull BrewingStandUpdateRequest updateContent() {
        return BrewingStandUpdateRequest.create(this, contentData);
    }

    /**************************
     *  Container properties  *
     *************************/
    
    public @NotNull BrewingStandContainer updateBrewTime(int value) {
        updateProperty(Property.BrewingStand.BREW_TIME, value);
        return this;
    }
    
    public @NotNull BrewingStandContainer updateFuelTime(int value) {
        updateProperty(Property.BrewingStand.FUEL_TIME, value);
        return this;
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, 4);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(5, 31);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(32, 40);
    }

    public interface BrewingStandUpdateRequest extends ContentUpdateRequest<BrewingStandContainer, BrewingStandUpdateRequest> {

        static @NotNull BrewingStandUpdateRequest create(
                @NotNull BrewingStandContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseBrewingStandUpdateRequest(container, contentData);
        }

        static @NotNull BrewingStandUpdateRequest create(
                @NotNull BrewingStandContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseBrewingStandUpdateRequest(container, contentData, slotsOffset);
        }

        // --- left bottle slot
        @NotNull BrewingStandUpdateRequest leftBottle(@Nullable ItemStack item);

        default @NotNull BrewingStandUpdateRequest leftBottle(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return leftBottle(new ItemStack(type, amount));
        }

        default @NotNull BrewingStandUpdateRequest leftBottle(@NotNull Material type) {
            return leftBottle(type, 1);
        }

        // --- center bottle slot
        @NotNull BrewingStandUpdateRequest centerBottle(@Nullable ItemStack item);

        default @NotNull BrewingStandUpdateRequest centerBottle(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return centerBottle(new ItemStack(type, amount));
        }

        default @NotNull BrewingStandUpdateRequest centerBottle(@NotNull Material type) {
            return centerBottle(type, 1);
        }

        // --- right bottle slot
        @NotNull BrewingStandUpdateRequest rightBottle(@Nullable ItemStack item);

        default @NotNull BrewingStandUpdateRequest rightBottle(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return rightBottle(new ItemStack(type, amount));
        }

        default @NotNull BrewingStandUpdateRequest rightBottle(@NotNull Material type) {
            return rightBottle(type, 1);
        }

        // --- potion ingredient slot
        @NotNull BrewingStandUpdateRequest potionIngredient(@Nullable ItemStack item);

        default @NotNull BrewingStandUpdateRequest potionIngredient(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return potionIngredient(new ItemStack(type, amount));
        }

        default @NotNull BrewingStandUpdateRequest potionIngredient(@NotNull Material type) {
            return potionIngredient(type, 1);
        }

        // --- blaze powder slot
        @NotNull BrewingStandUpdateRequest blazePowder(@Nullable ItemStack item);

        default @NotNull BrewingStandUpdateRequest blazePowder(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return blazePowder(new ItemStack(type, amount));
        }

        default @NotNull BrewingStandUpdateRequest blazePowder(@NotNull Material type) {
            return blazePowder(type, 1);
        }

    }

    private static final class BaseBrewingStandUpdateRequest extends BaseContentUpdateRequest<BrewingStandContainer, BrewingStandUpdateRequest> implements BrewingStandUpdateRequest {
        private BaseBrewingStandUpdateRequest(
                @NotNull BrewingStandContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseBrewingStandUpdateRequest(
                @NotNull BrewingStandContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull BrewingStandUpdateRequest leftBottle(@Nullable ItemStack item) {
            return set(item, LEFT_BOTTLE_SLOT, true);
        }

        @Override
        public @NotNull BrewingStandUpdateRequest centerBottle(@Nullable ItemStack item) {
            return set(item, CENTER_BOTTLE_SLOT, true);
        }

        @Override
        public @NotNull BrewingStandUpdateRequest rightBottle(@Nullable ItemStack item) {
            return set(item, RIGHT_BOTTLE_SLOT, true);
        }

        @Override
        public @NotNull BrewingStandUpdateRequest potionIngredient(@Nullable ItemStack item) {
            return set(item, POTION_INGREDIENT_SLOT, true);
        }

        @Override
        public @NotNull BrewingStandUpdateRequest blazePowder(@Nullable ItemStack item) {
            return set(item, BLAZE_POWDER_SLOT, true);
        }
    }
    
}
