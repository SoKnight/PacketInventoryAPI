package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.item.update.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

public class CraftingTableContainer extends Container<CraftingTableContainer, CraftingTableContainer.CraftingTableUpdateRequest> {

    public static final int RESULT_SLOT = 0;

    private CraftingTableContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.CRAFTING_TABLE, title);
    }

    private CraftingTableContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.CRAFTING_TABLE, title);
    }

    public static CraftingTableContainer create(Player inventoryHolder, String title) {
        return new CraftingTableContainer(inventoryHolder, title);
    }

    public static CraftingTableContainer create(Player inventoryHolder, BaseComponent title) {
        return new CraftingTableContainer(inventoryHolder, title);
    }

    public static CraftingTableContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.CRAFTING_TABLE);
    }

    @Override
    protected CraftingTableContainer getThis() {
        return this;
    }

    @Override
    public CraftingTableContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public CraftingTableUpdateRequest updateContent() {
        return CraftingTableUpdateRequest.create(this, contentData);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public CraftingTableContainer craftingGridClickListener(WindowClickListener<CraftingTableContainer, CraftingTableUpdateRequest> listener) {
        return clickListener(craftingGridSlots(), listener);
    }
    
    /*********************
     *  Inventory slots  *
     ********************/
    
    public static IntRange craftingGridSlots() {
        return new IntRange(1, 9);
    }
    
    public static int craftingGridSlot(int x, int y) {
        Validate.isTrue(x >= 0 && x <= 2, "'x' must be in the range '[0;2]'");
        Validate.isTrue(y >= 0 && y <= 2, "'y' must be in the range '[0;2]'");
        return x + 3 * y + 1;
    }

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, 9);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(10, 36);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(37, 45);
    }

    public interface CraftingTableUpdateRequest extends ContentUpdateRequest<CraftingTableContainer, CraftingTableUpdateRequest> {

        static CraftingTableUpdateRequest create(CraftingTableContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseCraftingTableUpdateRequest(container, contentData);
        }

        static CraftingTableUpdateRequest create(CraftingTableContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseCraftingTableUpdateRequest(container, contentData, slotsOffset);
        }

        CraftingTableUpdateRequest ingredient(ItemStack item, int slot);
        default CraftingTableUpdateRequest ingredient(Material type, int amount, int slot) { return ingredient(new ItemStack(type, amount), slot); }
        default CraftingTableUpdateRequest ingredient(Material type, int slot) { return ingredient(type, 1, slot); }

        CraftingTableUpdateRequest ingredientAt(ItemStack item, int x, int y);
        default CraftingTableUpdateRequest ingredientAt(Material type, int amount, int x, int y) { return ingredientAt(new ItemStack(type, amount), x, y); }
        default CraftingTableUpdateRequest ingredientAt(Material type, int x, int y) { return ingredientAt(type, 1, x, y); }

        CraftingTableUpdateRequest resultItem(ItemStack item);
        default CraftingTableUpdateRequest resultItem(Material type, int amount) { return resultItem(new ItemStack(type, amount)); }
        default CraftingTableUpdateRequest resultItem(Material type) { return resultItem(type, 1); }

    }

    private static final class BaseCraftingTableUpdateRequest extends BaseContentUpdateRequest<CraftingTableContainer, CraftingTableUpdateRequest> implements CraftingTableUpdateRequest {
        private BaseCraftingTableUpdateRequest(CraftingTableContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseCraftingTableUpdateRequest(CraftingTableContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public CraftingTableUpdateRequest ingredient(ItemStack item, int slot) {
            return set(item, slot, true);
        }

        @Override
        public CraftingTableUpdateRequest ingredientAt(ItemStack item, int x, int y) {
            return set(item, craftingGridSlot(x, y), true);
        }

        @Override
        public CraftingTableUpdateRequest resultItem(ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
