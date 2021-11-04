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

public class HopperContainer extends Container<HopperContainer, HopperContainer.HopperUpdateRequest> {

    private HopperContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.HOPPER, title);
    }

    private HopperContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.HOPPER, title);
    }

    public static @NotNull HopperContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new HopperContainer(inventoryHolder, title);
    }

    public static @NotNull HopperContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new HopperContainer(inventoryHolder, title);
    }

    public static @NotNull HopperContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.HOPPER);
    }

    @Override
    protected @NotNull HopperContainer getThis() {
        return this;
    }

    @Override
    public @NotNull HopperContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull HopperUpdateRequest updateContent() {
        return HopperUpdateRequest.create(this, contentData);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public @NotNull HopperContainer hopperClickListener(int slot, @NotNull WindowClickListener<HopperContainer, HopperUpdateRequest> listener) {
        return clickListener(hopperSlot(slot), listener);
    }
    
    /****************************
     *  Hopper inventory slots  *
     ***************************/
    
    public static @NotNull IntRange hopperSlots() {
        return new IntRange(0, 4);
    }
    
    public static int hopperSlot(int x) {
        if(x < 0 || x > 4)
            throw new IllegalArgumentException("'x' must be in the range '0-4' (inclusive)");
        
        return x;
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

    public interface HopperUpdateRequest extends ContentUpdateRequest<HopperContainer, HopperUpdateRequest> {

        static @NotNull HopperUpdateRequest create(
                @NotNull HopperContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseHopperUpdateRequest(container, contentData);
        }

        static @NotNull HopperUpdateRequest create(
                @NotNull HopperContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseHopperUpdateRequest(container, contentData, slotsOffset);
        }

        // --- hopper item slots
        @NotNull HopperUpdateRequest hopperItem(@Nullable ItemStack item, int slot);

        default @NotNull HopperUpdateRequest hopperItem(@NotNull Material type, int amount, int slot) {
            Validate.notNull(type, "type");
            return hopperItem(new ItemStack(type, amount), slot);
        }

        default @NotNull HopperUpdateRequest hopperItem(@NotNull Material type, int slot) {
            return hopperItem(type, 1, slot);
        }

    }

    private static final class BaseHopperUpdateRequest extends BaseContentUpdateRequest<HopperContainer, HopperUpdateRequest> implements HopperUpdateRequest {
        private BaseHopperUpdateRequest(
                @NotNull HopperContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseHopperUpdateRequest(
                @NotNull HopperContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull HopperUpdateRequest hopperItem(@Nullable ItemStack item, int slot) {
            return set(item, hopperSlot(slot), true);
        }
    }
    
}
