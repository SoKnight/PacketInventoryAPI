package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

public class HopperContainer extends Container<HopperContainer, HopperContainer.HopperUpdateRequest> {

    private HopperContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.HOPPER, title);
    }

    private HopperContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.HOPPER, title);
    }

    public static HopperContainer create(Player inventoryHolder, String title) {
        return new HopperContainer(inventoryHolder, title);
    }

    public static HopperContainer create(Player inventoryHolder, BaseComponent title) {
        return new HopperContainer(inventoryHolder, title);
    }

    public static HopperContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.HOPPER);
    }

    @Override
    protected HopperContainer getThis() {
        return this;
    }

    @Override
    public HopperContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public HopperUpdateRequest updateContent() {
        return HopperUpdateRequest.create(this, contentData);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public HopperContainer hopperClickListener(int slot, WindowClickListener<HopperContainer, HopperUpdateRequest> listener) {
        super.clickListener(hopperSlot(slot), listener);
        return this;
    }
    
    /*********************
     *  Inventory slots  *
     ********************/
    
    public static IntRange hopperSlots() {
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

    public interface HopperUpdateRequest extends ContentUpdateRequest<HopperContainer, HopperUpdateRequest> {

        static HopperUpdateRequest create(HopperContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseHopperUpdateRequest(container, contentData);
        }

        static HopperUpdateRequest create(HopperContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseHopperUpdateRequest(container, contentData, slotsOffset);
        }

        HopperUpdateRequest hopperItem(ItemStack item, int slot);
        default HopperUpdateRequest hopperItem(Material type, int amount, int slot) { return hopperItem(new ItemStack(type, amount), slot); }
        default HopperUpdateRequest hopperItem(Material type, int slot) { return hopperItem(type, 1, slot); }

    }

    private static final class BaseHopperUpdateRequest extends BaseContentUpdateRequest<HopperContainer, HopperUpdateRequest> implements HopperUpdateRequest {
        private BaseHopperUpdateRequest(HopperContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseHopperUpdateRequest(HopperContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public HopperUpdateRequest hopperItem(ItemStack item, int slot) {
            return set(item, hopperSlot(slot), true);
        }
    }
    
}
