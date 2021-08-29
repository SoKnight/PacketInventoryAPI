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
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

public class CartographyTableContainer extends Container<CartographyTableContainer, CartographyTableContainer.CartographyTableUpdateRequest> {

    public static final int MAP_SLOT = 0;
    public static final int PAPER_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    private CartographyTableContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.CARTOGRAPHY_TABLE, title);
    }

    private CartographyTableContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.CARTOGRAPHY_TABLE, title);
    }

    public static CartographyTableContainer create(Player inventoryHolder, String title) {
        return new CartographyTableContainer(inventoryHolder, title);
    }

    public static CartographyTableContainer create(Player inventoryHolder, BaseComponent title) {
        return new CartographyTableContainer(inventoryHolder, title);
    }

    public static CartographyTableContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.CARTOGRAPHY_TABLE);
    }

    @Override
    protected CartographyTableContainer getThis() {
        return this;
    }

    @Override
    public CartographyTableContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public CartographyTableUpdateRequest updateContent() {
        return CartographyTableUpdateRequest.create(this, contentData);
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

    public interface CartographyTableUpdateRequest extends ContentUpdateRequest<CartographyTableContainer, CartographyTableUpdateRequest> {

        static CartographyTableUpdateRequest create(CartographyTableContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseCartographyTableUpdateRequest(container, contentData);
        }

        static CartographyTableUpdateRequest create(CartographyTableContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseCartographyTableUpdateRequest(container, contentData, slotsOffset);
        }

        CartographyTableUpdateRequest map(ItemStack item);
        default CartographyTableUpdateRequest map(Material type, int amount) { return map(new ItemStack(type, amount)); }
        default CartographyTableUpdateRequest map(Material type) { return map(type, 1); }

        CartographyTableUpdateRequest paper(ItemStack item);
        default CartographyTableUpdateRequest paper(Material type, int amount) { return paper(new ItemStack(type, amount)); }
        default CartographyTableUpdateRequest paper(Material type) { return paper(type, 1); }

        CartographyTableUpdateRequest outputItem(ItemStack item);
        default CartographyTableUpdateRequest outputItem(Material type, int amount) { return outputItem(new ItemStack(type, amount)); }
        default CartographyTableUpdateRequest outputItem(Material type) { return outputItem(type, 1); }

    }

    private static final class BaseCartographyTableUpdateRequest extends BaseContentUpdateRequest<CartographyTableContainer, CartographyTableUpdateRequest> implements CartographyTableUpdateRequest {
        private BaseCartographyTableUpdateRequest(CartographyTableContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseCartographyTableUpdateRequest(CartographyTableContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public CartographyTableUpdateRequest map(ItemStack item) {
            return set(item, MAP_SLOT, true);
        }

        @Override
        public CartographyTableUpdateRequest paper(ItemStack item) {
            return set(item, PAPER_SLOT, true);
        }

        @Override
        public CartographyTableUpdateRequest outputItem(ItemStack item) {
            return set(item, OUTPUT_SLOT, true);
        }
    }
    
}
