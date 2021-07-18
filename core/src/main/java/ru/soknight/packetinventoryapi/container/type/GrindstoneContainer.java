package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.item.update.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

public class GrindstoneContainer extends Container<GrindstoneContainer, GrindstoneContainer.GrindstoneUpdateRequest> {

    public static final int FIRST_ITEM_SLOT = 0;
    public static final int SECOND_ITEM_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    private GrindstoneContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.GRINDSTONE, title);
    }

    private GrindstoneContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.GRINDSTONE, title);
    }

    public static GrindstoneContainer create(Player inventoryHolder, String title) {
        return new GrindstoneContainer(inventoryHolder, title);
    }

    public static GrindstoneContainer create(Player inventoryHolder, BaseComponent title) {
        return new GrindstoneContainer(inventoryHolder, title);
    }

    public static GrindstoneContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.GRINDSTONE);
    }

    @Override
    protected GrindstoneContainer getThis() {
        return this;
    }

    @Override
    public GrindstoneContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public GrindstoneUpdateRequest updateContent() {
        return GrindstoneUpdateRequest.create(this, contentData);
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, 2);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(3, 29);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(30, 38);
    }

    public interface GrindstoneUpdateRequest extends ContentUpdateRequest<GrindstoneContainer, GrindstoneUpdateRequest> {

        static GrindstoneUpdateRequest create(GrindstoneContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseGrindstoneUpdateRequest(container, contentData);
        }

        static GrindstoneUpdateRequest create(GrindstoneContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseGrindstoneUpdateRequest(container, contentData, slotsOffset);
        }

        GrindstoneUpdateRequest firstItem(ItemStack item);
        default GrindstoneUpdateRequest firstItem(Material type, int amount) { return firstItem(new ItemStack(type, amount)); }
        default GrindstoneUpdateRequest firstItem(Material type) { return firstItem(type, 1); }

        GrindstoneUpdateRequest secondItem(ItemStack item);
        default GrindstoneUpdateRequest secondItem(Material type, int amount) { return secondItem(new ItemStack(type, amount)); }
        default GrindstoneUpdateRequest secondItem(Material type) { return secondItem(type, 1); }

        GrindstoneUpdateRequest resultItem(ItemStack item);
        default GrindstoneUpdateRequest resultItem(Material type, int amount) { return resultItem(new ItemStack(type, amount)); }
        default GrindstoneUpdateRequest resultItem(Material type) { return resultItem(type, 1); }

    }

    private static final class BaseGrindstoneUpdateRequest extends BaseContentUpdateRequest<GrindstoneContainer, GrindstoneUpdateRequest> implements GrindstoneUpdateRequest {
        private BaseGrindstoneUpdateRequest(GrindstoneContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseGrindstoneUpdateRequest(GrindstoneContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public GrindstoneUpdateRequest firstItem(ItemStack item) {
            return set(item, FIRST_ITEM_SLOT, true);
        }

        @Override
        public GrindstoneUpdateRequest secondItem(ItemStack item) {
            return set(item, SECOND_ITEM_SLOT, true);
        }

        @Override
        public GrindstoneUpdateRequest resultItem(ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
