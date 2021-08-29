package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;
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

public class DropperContainer extends Container<DropperContainer, DropperContainer.DropperUpdateRequest> {

    private DropperContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.GENERIC_3X3, title);
    }

    private DropperContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.GENERIC_3X3, title);
    }

    public static DropperContainer create(Player inventoryHolder, String title) {
        return new DropperContainer(inventoryHolder, title);
    }

    public static DropperContainer create(Player inventoryHolder, BaseComponent title) {
        return new DropperContainer(inventoryHolder, title);
    }

    public static DropperContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.DROPPER);
    }

    @Override
    protected DropperContainer getThis() {
        return this;
    }

    @Override
    public DropperContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public DropperUpdateRequest updateContent() {
        return DropperUpdateRequest.create(this, contentData);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public DropperContainer dropperGridClickListener(WindowClickListener<DropperContainer, DropperUpdateRequest> listener) {
        return clickListener(dropperGridSlots(), listener);
    }
    
    /*********************
     *  Inventory slots  *
     ********************/
    
    public IntRange dropperGridSlots() {
        return new IntRange(0, 8);
    }
    
    public static int dropperGridSlot(int x, int y) {
        Validate.isTrue(x >= 0 && x <= 2, "'x' must be in the range '[0;2]'");
        Validate.isTrue(y >= 0 && y <= 2, "'y' must be in the range '[0;2]'");
        return x + 3 * y;
    }

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, 8);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(9, 35);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(36, 44);
    }

    public interface DropperUpdateRequest extends ContentUpdateRequest<DropperContainer, DropperUpdateRequest> {

        static DropperUpdateRequest create(DropperContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseDropperUpdateRequest(container, contentData);
        }

        static DropperUpdateRequest create(DropperContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseDropperUpdateRequest(container, contentData, slotsOffset);
        }

        DropperUpdateRequest ingredient(ItemStack item, int slot);
        default DropperUpdateRequest ingredient(Material type, int amount, int slot) { return ingredient(new ItemStack(type, amount), slot); }
        default DropperUpdateRequest ingredient(Material type, int slot) { return ingredient(type, 1, slot); }

        DropperUpdateRequest ingredientAt(ItemStack item, int x, int y);
        default DropperUpdateRequest ingredientAt(Material type, int amount, int x, int y) { return ingredientAt(new ItemStack(type, amount), x, y); }
        default DropperUpdateRequest ingredientAt(Material type, int x, int y) { return ingredientAt(type, 1, x, y); }

    }

    private static final class BaseDropperUpdateRequest extends BaseContentUpdateRequest<DropperContainer, DropperUpdateRequest> implements DropperUpdateRequest {
        private BaseDropperUpdateRequest(DropperContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseDropperUpdateRequest(DropperContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public DropperUpdateRequest ingredient(ItemStack item, int slot) {
            return set(item, slot, true);
        }

        @Override
        public DropperUpdateRequest ingredientAt(ItemStack item, int x, int y) {
            return set(item, dropperGridSlot(x, y), true);
        }
    }
    
}
