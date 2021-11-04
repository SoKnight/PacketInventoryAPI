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
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

public class CraftingTableContainer extends Container<CraftingTableContainer, CraftingTableContainer.CraftingTableUpdateRequest> {

    public static final int RESULT_SLOT = 0;

    private CraftingTableContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.CRAFTING_TABLE, title);
    }

    private CraftingTableContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.CRAFTING_TABLE, title);
    }

    public static @NotNull CraftingTableContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new CraftingTableContainer(inventoryHolder, title);
    }

    public static @NotNull CraftingTableContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new CraftingTableContainer(inventoryHolder, title);
    }

    public static @NotNull CraftingTableContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.CRAFTING_TABLE);
    }

    @Override
    protected @NotNull CraftingTableContainer getThis() {
        return this;
    }

    @Override
    public @NotNull CraftingTableContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull CraftingTableUpdateRequest updateContent() {
        return CraftingTableUpdateRequest.create(this, contentData);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public @NotNull CraftingTableContainer craftingGridClickListener(@NotNull WindowClickListener<CraftingTableContainer, CraftingTableUpdateRequest> listener) {
        return clickListener(craftingGridSlots(), listener);
    }
    
    /*********************
     *  Inventory slots  *
     ********************/
    
    public static @NotNull IntRange craftingGridSlots() {
        return new IntRange(1, 9);
    }
    
    public static int craftingGridSlot(int x, int y) {
        Validate.isTrue(x >= 0 && x <= 2, "'x' must be in the range '[0;2]'");
        Validate.isTrue(y >= 0 && y <= 2, "'y' must be in the range '[0;2]'");
        return x + 3 * y + 1;
    }

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, 9);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(10, 36);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(37, 45);
    }

    public interface CraftingTableUpdateRequest extends ContentUpdateRequest<CraftingTableContainer, CraftingTableUpdateRequest> {

        static @NotNull CraftingTableUpdateRequest create(
                @NotNull CraftingTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseCraftingTableUpdateRequest(container, contentData);
        }

        static @NotNull CraftingTableUpdateRequest create(
                @NotNull CraftingTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseCraftingTableUpdateRequest(container, contentData, slotsOffset);
        }

        // --- ingredient slot
        @NotNull CraftingTableUpdateRequest ingredient(@Nullable ItemStack item, int slot);

        default @NotNull CraftingTableUpdateRequest ingredient(@NotNull Material type, int amount, int slot) {
            Validate.notNull(type, "type");
            return ingredient(new ItemStack(type, amount), slot);
        }

        default @NotNull CraftingTableUpdateRequest ingredient(@NotNull Material type, int slot) {
            return ingredient(type, 1, slot);
        }

        // --- ingredient slot by XY axis
        @NotNull CraftingTableUpdateRequest ingredientAt(@Nullable ItemStack item, int x, int y);

        default @NotNull CraftingTableUpdateRequest ingredientAt(@NotNull Material type, int amount, int x, int y) {
            Validate.notNull(type, "type");
            return ingredientAt(new ItemStack(type, amount), x, y);
        }

        default @NotNull CraftingTableUpdateRequest ingredientAt(@NotNull Material type, int x, int y) {
            return ingredientAt(type, 1, x, y);
        }

        // --- result slot
        @NotNull CraftingTableUpdateRequest resultItem(@Nullable ItemStack item);

        default @NotNull CraftingTableUpdateRequest resultItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return resultItem(new ItemStack(type, amount));
        }

        default @NotNull CraftingTableUpdateRequest resultItem(@NotNull Material type) {
            return resultItem(type, 1);
        }

    }

    private static final class BaseCraftingTableUpdateRequest extends BaseContentUpdateRequest<CraftingTableContainer, CraftingTableUpdateRequest> implements CraftingTableUpdateRequest {
        private BaseCraftingTableUpdateRequest(
                @NotNull CraftingTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseCraftingTableUpdateRequest(
                @NotNull CraftingTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull CraftingTableUpdateRequest ingredient(@Nullable ItemStack item, int slot) {
            return set(item, slot, true);
        }

        @Override
        public @NotNull CraftingTableUpdateRequest ingredientAt(@Nullable ItemStack item, int x, int y) {
            return set(item, craftingGridSlot(x, y), true);
        }

        @Override
        public @NotNull CraftingTableUpdateRequest resultItem(@Nullable ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
