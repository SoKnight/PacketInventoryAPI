package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
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
import ru.soknight.packetinventoryapi.event.container.AnvilRenameEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.AnvilRenameListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

@Getter
public class AnvilContainer extends Container<AnvilContainer, AnvilContainer.AnvilUpdateRequest> {

    public static final int FIRST_ITEM_SLOT = 0;
    public static final int SECOND_ITEM_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    
    private @Nullable EventListener<AnvilRenameEvent> itemRenameListener;
    private @Nullable String customName;

    private AnvilContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.ANVIL, title);
    }

    private AnvilContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.ANVIL, title);
    }

    public static @NotNull AnvilContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new AnvilContainer(inventoryHolder, title);
    }

    public static @NotNull AnvilContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new AnvilContainer(inventoryHolder, title);
    }

    public static @NotNull AnvilContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.ANVIL);
    }

    @Override
    protected @NotNull AnvilContainer getThis() {
        return this;
    }

    @Override
    public @NotNull AnvilContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull AnvilUpdateRequest updateContent() {
        return AnvilUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(@NotNull AnvilContainer clone, @NotNull AnyEventListener listener) {
        clone.itemRenameListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- item renaming listening
    public @NotNull AnvilContainer itemRenameListener(@Nullable AnvilRenameListener listener) {
        this.itemRenameListener = listener;
        return this;
    }
    
    public void onItemRename(@NotNull AnvilRenameEvent event) {
        this.customName = event.getCustomName();
        
        if(itemRenameListener != null)
            itemRenameListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public @NotNull AnvilContainer updateRepairCost(int value) {
        updateProperty(Property.Anvil.REPAIR_COST, value);
        return this;
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

    public interface AnvilUpdateRequest extends ContentUpdateRequest<AnvilContainer, AnvilUpdateRequest> {

        static @NotNull AnvilUpdateRequest create(
                @NotNull AnvilContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseAnvilUpdateRequest(container, contentData);
        }

        static @NotNull AnvilUpdateRequest create(
                @NotNull AnvilContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseAnvilUpdateRequest(container, contentData, slotsOffset);
        }

        // --- first item slot
        @NotNull AnvilUpdateRequest firstItem(@Nullable ItemStack item);

        default @NotNull AnvilUpdateRequest firstItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return firstItem(new ItemStack(type, amount));
        }

        default @NotNull AnvilUpdateRequest firstItem(@NotNull Material type) {
            return firstItem(type, 1);
        }

        // --- second item slot
        @NotNull AnvilUpdateRequest secondItem(@Nullable ItemStack item);

        default @NotNull AnvilUpdateRequest secondItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return secondItem(new ItemStack(type, amount));
        }

        default @NotNull AnvilUpdateRequest secondItem(@NotNull Material type) {
            return secondItem(type, 1);
        }

        // --- result item slot
        @NotNull AnvilUpdateRequest resultItem(@Nullable ItemStack item);

        default @NotNull AnvilUpdateRequest resultItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return resultItem(new ItemStack(type, amount));
        }

        default @NotNull AnvilUpdateRequest resultItem(@NotNull Material type) {
            return resultItem(type, 1);
        }

    }

    private static final class BaseAnvilUpdateRequest extends BaseContentUpdateRequest<AnvilContainer, AnvilUpdateRequest> implements AnvilUpdateRequest {
        private BaseAnvilUpdateRequest(
                @NotNull AnvilContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseAnvilUpdateRequest(
                @NotNull AnvilContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull AnvilUpdateRequest firstItem(@Nullable ItemStack item) {
            return set(item, FIRST_ITEM_SLOT, true);
        }

        @Override
        public @NotNull AnvilUpdateRequest secondItem(@Nullable ItemStack item) {
            return set(item, SECOND_ITEM_SLOT, true);
        }

        @Override
        public @NotNull AnvilUpdateRequest resultItem(@Nullable ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }

}
