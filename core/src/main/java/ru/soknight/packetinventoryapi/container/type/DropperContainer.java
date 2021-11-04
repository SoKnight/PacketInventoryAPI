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

public class DropperContainer extends Container<DropperContainer, DropperContainer.DropperUpdateRequest> {

    private DropperContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.GENERIC_3X3, title);
    }

    private DropperContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.GENERIC_3X3, title);
    }

    public static @NotNull DropperContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new DropperContainer(inventoryHolder, title);
    }

    public static @NotNull DropperContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new DropperContainer(inventoryHolder, title);
    }

    public static @NotNull DropperContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.DROPPER);
    }

    @Override
    protected @NotNull DropperContainer getThis() {
        return this;
    }

    @Override
    public @NotNull DropperContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull DropperUpdateRequest updateContent() {
        return DropperUpdateRequest.create(this, contentData);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public @NotNull DropperContainer dropperGridClickListener(@NotNull WindowClickListener<DropperContainer, DropperUpdateRequest> listener) {
        return clickListener(dropperGridSlots(), listener);
    }
    
    /*********************
     *  Inventory slots  *
     ********************/
    
    public @NotNull IntRange dropperGridSlots() {
        return new IntRange(0, 8);
    }
    
    public static int dropperGridSlot(int x, int y) {
        Validate.isTrue(x >= 0 && x <= 2, "'x' must be in the range '[0;2]'");
        Validate.isTrue(y >= 0 && y <= 2, "'y' must be in the range '[0;2]'");
        return x + 3 * y;
    }

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, 8);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(9, 35);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(36, 44);
    }

    public interface DropperUpdateRequest extends ContentUpdateRequest<DropperContainer, DropperUpdateRequest> {

        static @NotNull DropperUpdateRequest create(
                @NotNull DropperContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseDropperUpdateRequest(container, contentData);
        }

        static @NotNull DropperUpdateRequest create(
                @NotNull DropperContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseDropperUpdateRequest(container, contentData, slotsOffset);
        }

        // --- ingredient slot
        @NotNull DropperUpdateRequest ingredient(@Nullable ItemStack item, int slot);

        default @NotNull DropperUpdateRequest ingredient(@NotNull Material type, int amount, int slot) {
            Validate.notNull(type, "type");
            return ingredient(new ItemStack(type, amount), slot);
        }

        default @NotNull DropperUpdateRequest ingredient(@NotNull Material type, int slot) {
            return ingredient(type, 1, slot);
        }

        // --- ingredient slot by XY axis
        @NotNull DropperUpdateRequest ingredientAt(@Nullable ItemStack item, int x, int y);

        default @NotNull DropperUpdateRequest ingredientAt(@NotNull Material type, int amount, int x, int y) {
            Validate.notNull(type, "type");
            return ingredientAt(new ItemStack(type, amount), x, y);
        }

        default @NotNull DropperUpdateRequest ingredientAt(@NotNull Material type, int x, int y) {
            return ingredientAt(type, 1, x, y);
        }

    }

    private static final class BaseDropperUpdateRequest extends BaseContentUpdateRequest<DropperContainer, DropperUpdateRequest> implements DropperUpdateRequest {
        private BaseDropperUpdateRequest(
                @NotNull DropperContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseDropperUpdateRequest(
                @NotNull DropperContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull DropperUpdateRequest ingredient(@Nullable ItemStack item, int slot) {
            return set(item, slot, true);
        }

        @Override
        public @NotNull DropperUpdateRequest ingredientAt(@Nullable ItemStack item, int x, int y) {
            return set(item, dropperGridSlot(x, y), true);
        }
    }
    
}
