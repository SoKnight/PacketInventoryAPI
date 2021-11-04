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
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

public class CartographyTableContainer extends Container<CartographyTableContainer, CartographyTableContainer.CartographyTableUpdateRequest> {

    public static final int MAP_SLOT = 0;
    public static final int PAPER_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    private CartographyTableContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.CARTOGRAPHY_TABLE, title);
    }

    private CartographyTableContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.CARTOGRAPHY_TABLE, title);
    }

    public static @NotNull CartographyTableContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new CartographyTableContainer(inventoryHolder, title);
    }

    public static @NotNull CartographyTableContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new CartographyTableContainer(inventoryHolder, title);
    }

    public static @NotNull CartographyTableContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.CARTOGRAPHY_TABLE);
    }

    @Override
    protected @NotNull CartographyTableContainer getThis() {
        return this;
    }

    @Override
    public @NotNull CartographyTableContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull CartographyTableUpdateRequest updateContent() {
        return CartographyTableUpdateRequest.create(this, contentData);
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

    public interface CartographyTableUpdateRequest extends ContentUpdateRequest<CartographyTableContainer, CartographyTableUpdateRequest> {

        static @NotNull CartographyTableUpdateRequest create(
                @NotNull CartographyTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseCartographyTableUpdateRequest(container, contentData);
        }

        static @NotNull CartographyTableUpdateRequest create(
                @NotNull CartographyTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseCartographyTableUpdateRequest(container, contentData, slotsOffset);
        }

        // --- map slot
        @NotNull CartographyTableUpdateRequest map(@Nullable ItemStack item);

        default @NotNull CartographyTableUpdateRequest map(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return map(new ItemStack(type, amount));
        }

        default @NotNull CartographyTableUpdateRequest map(@NotNull Material type) {
            return map(type, 1);
        }

        // --- paper slot
        @NotNull CartographyTableUpdateRequest paper(@Nullable ItemStack item);

        default @NotNull CartographyTableUpdateRequest paper(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return paper(new ItemStack(type, amount));
        }

        default @NotNull CartographyTableUpdateRequest paper(@NotNull Material type) {
            return paper(type, 1);
        }

        // --- output item slot
        @NotNull CartographyTableUpdateRequest outputItem(@Nullable ItemStack item);

        default @NotNull CartographyTableUpdateRequest outputItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return outputItem(new ItemStack(type, amount));
        }

        default @NotNull CartographyTableUpdateRequest outputItem(@NotNull Material type) {
            return outputItem(type, 1);
        }

    }

    private static final class BaseCartographyTableUpdateRequest extends BaseContentUpdateRequest<CartographyTableContainer, CartographyTableUpdateRequest> implements CartographyTableUpdateRequest {
        private BaseCartographyTableUpdateRequest(
                @NotNull CartographyTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseCartographyTableUpdateRequest(
                @NotNull CartographyTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull CartographyTableUpdateRequest map(@Nullable ItemStack item) {
            return set(item, MAP_SLOT, true);
        }

        @Override
        public @NotNull CartographyTableUpdateRequest paper(@Nullable ItemStack item) {
            return set(item, PAPER_SLOT, true);
        }

        @Override
        public @NotNull CartographyTableUpdateRequest outputItem(@Nullable ItemStack item) {
            return set(item, OUTPUT_SLOT, true);
        }
    }
    
}
