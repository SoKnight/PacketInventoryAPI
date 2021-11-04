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

public class GrindstoneContainer extends Container<GrindstoneContainer, GrindstoneContainer.GrindstoneUpdateRequest> {

    public static final int FIRST_ITEM_SLOT = 0;
    public static final int SECOND_ITEM_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    private GrindstoneContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.GRINDSTONE, title);
    }

    private GrindstoneContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.GRINDSTONE, title);
    }

    public static @NotNull GrindstoneContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new GrindstoneContainer(inventoryHolder, title);
    }

    public static @NotNull GrindstoneContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new GrindstoneContainer(inventoryHolder, title);
    }

    public static @NotNull GrindstoneContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.GRINDSTONE);
    }

    @Override
    protected @NotNull GrindstoneContainer getThis() {
        return this;
    }

    @Override
    public @NotNull GrindstoneContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull GrindstoneUpdateRequest updateContent() {
        return GrindstoneUpdateRequest.create(this, contentData);
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

    public interface GrindstoneUpdateRequest extends ContentUpdateRequest<GrindstoneContainer, GrindstoneUpdateRequest> {

        static @NotNull GrindstoneUpdateRequest create(
                @NotNull GrindstoneContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseGrindstoneUpdateRequest(container, contentData);
        }

        static @NotNull GrindstoneUpdateRequest create(
                @NotNull GrindstoneContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseGrindstoneUpdateRequest(container, contentData, slotsOffset);
        }

        // --- first item slot
        @NotNull GrindstoneUpdateRequest firstItem(@Nullable ItemStack item);

        default @NotNull GrindstoneUpdateRequest firstItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return firstItem(new ItemStack(type, amount));
        }

        default @NotNull GrindstoneUpdateRequest firstItem(@NotNull Material type) {
            return firstItem(type, 1);
        }

        // --- second item slot
        @NotNull GrindstoneUpdateRequest secondItem(@Nullable ItemStack item);

        default @NotNull GrindstoneUpdateRequest secondItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return secondItem(new ItemStack(type, amount));
        }

        default @NotNull GrindstoneUpdateRequest secondItem(@NotNull Material type) {
            return secondItem(type, 1);
        }

        // --- result item slot
        @NotNull GrindstoneUpdateRequest resultItem(@Nullable ItemStack item);

        default @NotNull GrindstoneUpdateRequest resultItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return resultItem(new ItemStack(type, amount));
        }

        default @NotNull GrindstoneUpdateRequest resultItem(@NotNull Material type) {
            return resultItem(type, 1);
        }

    }

    private static final class BaseGrindstoneUpdateRequest extends BaseContentUpdateRequest<GrindstoneContainer, GrindstoneUpdateRequest> implements GrindstoneUpdateRequest {
        private BaseGrindstoneUpdateRequest(
                @NotNull GrindstoneContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseGrindstoneUpdateRequest(
                @NotNull GrindstoneContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull GrindstoneUpdateRequest firstItem(@Nullable ItemStack item) {
            return set(item, FIRST_ITEM_SLOT, true);
        }

        @Override
        public @NotNull GrindstoneUpdateRequest secondItem(@Nullable ItemStack item) {
            return set(item, SECOND_ITEM_SLOT, true);
        }

        @Override
        public @NotNull GrindstoneUpdateRequest resultItem(@Nullable ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
