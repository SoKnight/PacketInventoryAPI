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

public class SmithingTableContainer extends Container<SmithingTableContainer, SmithingTableContainer.SmithingTableUpdateRequest> {

    public static final int ITEM_TO_UPGRADE_SLOT = 0;
    public static final int NETHERITE_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    private SmithingTableContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.SMITHING_TABLE, title);
    }

    private SmithingTableContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.SMITHING_TABLE, title);
    }

    public static @NotNull SmithingTableContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new SmithingTableContainer(inventoryHolder, title);
    }

    public static @NotNull SmithingTableContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new SmithingTableContainer(inventoryHolder, title);
    }

    public static @NotNull SmithingTableContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.SMITHING_TABLE);
    }

    @Override
    protected @NotNull SmithingTableContainer getThis() {
        return this;
    }

    @Override
    public @NotNull SmithingTableContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull SmithingTableUpdateRequest updateContent() {
        return SmithingTableUpdateRequest.create(this, contentData);
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

    public interface SmithingTableUpdateRequest extends ContentUpdateRequest<SmithingTableContainer, SmithingTableUpdateRequest> {

        static @NotNull SmithingTableUpdateRequest create(
                @NotNull SmithingTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseSmithingTableUpdateRequest(container, contentData);
        }

        static @NotNull SmithingTableUpdateRequest create(
                @NotNull SmithingTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseSmithingTableUpdateRequest(container, contentData, slotsOffset);
        }

        // --- item to upgrade slot
        @NotNull SmithingTableUpdateRequest itemToUpgrade(@Nullable ItemStack item);

        default @NotNull SmithingTableUpdateRequest itemToUpgrade(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return itemToUpgrade(new ItemStack(type, amount));
        }

        default @NotNull SmithingTableUpdateRequest itemToUpgrade(@NotNull Material type) {
            return itemToUpgrade(type, 1);
        }

        // --- netherite item slot
        @NotNull SmithingTableUpdateRequest netherite(@Nullable ItemStack item);

        default @NotNull SmithingTableUpdateRequest netherite(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return netherite(new ItemStack(type, amount));
        }

        default @NotNull SmithingTableUpdateRequest netherite(@NotNull Material type) {
            return netherite(type, 1);
        }

        // --- result item slot
        @NotNull SmithingTableUpdateRequest resultItem(@Nullable ItemStack item);

        default @NotNull SmithingTableUpdateRequest resultItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return resultItem(new ItemStack(type, amount));
        }

        default @NotNull SmithingTableUpdateRequest resultItem(@NotNull Material type) {
            return resultItem(type, 1);
        }

    }

    private static final class BaseSmithingTableUpdateRequest extends BaseContentUpdateRequest<SmithingTableContainer, SmithingTableUpdateRequest> implements SmithingTableUpdateRequest {
        private BaseSmithingTableUpdateRequest(
                @NotNull SmithingTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseSmithingTableUpdateRequest(
                @NotNull SmithingTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull SmithingTableUpdateRequest itemToUpgrade(@Nullable ItemStack item) {
            return set(item, ITEM_TO_UPGRADE_SLOT, true);
        }

        @Override
        public @NotNull SmithingTableUpdateRequest netherite(@Nullable ItemStack item) {
            return set(item, NETHERITE_SLOT, true);
        }

        @Override
        public @NotNull SmithingTableUpdateRequest resultItem(@Nullable ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
