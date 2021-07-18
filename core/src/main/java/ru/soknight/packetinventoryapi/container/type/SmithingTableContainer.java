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

public class SmithingTableContainer extends Container<SmithingTableContainer, SmithingTableContainer.SmithingTableUpdateRequest> {

    public static final int ITEM_TO_UPGRADE_SLOT = 0;
    public static final int NETHERITE_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    private SmithingTableContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.SMITHING_TABLE, title);
    }

    private SmithingTableContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.SMITHING_TABLE, title);
    }

    public static SmithingTableContainer create(Player inventoryHolder, String title) {
        return new SmithingTableContainer(inventoryHolder, title);
    }

    public static SmithingTableContainer create(Player inventoryHolder, BaseComponent title) {
        return new SmithingTableContainer(inventoryHolder, title);
    }

    public static SmithingTableContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.SMITHING_TABLE);
    }

    @Override
    protected SmithingTableContainer getThis() {
        return this;
    }

    @Override
    public SmithingTableContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public SmithingTableUpdateRequest updateContent() {
        return SmithingTableUpdateRequest.create(this, contentData);
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

    public interface SmithingTableUpdateRequest extends ContentUpdateRequest<SmithingTableContainer, SmithingTableUpdateRequest> {

        static SmithingTableUpdateRequest create(SmithingTableContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseSmithingTableUpdateRequest(container, contentData);
        }

        static SmithingTableUpdateRequest create(SmithingTableContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseSmithingTableUpdateRequest(container, contentData, slotsOffset);
        }

        SmithingTableUpdateRequest itemToUpgrade(ItemStack item);
        default SmithingTableUpdateRequest itemToUpgrade(Material type, int amount) { return itemToUpgrade(new ItemStack(type, amount)); }
        default SmithingTableUpdateRequest itemToUpgrade(Material type) { return itemToUpgrade(type, 1); }

        SmithingTableUpdateRequest netherite(ItemStack item);
        default SmithingTableUpdateRequest netherite(Material type, int amount) { return netherite(new ItemStack(type, amount)); }
        default SmithingTableUpdateRequest netherite(Material type) { return netherite(type, 1); }

        SmithingTableUpdateRequest resultItem(ItemStack item);
        default SmithingTableUpdateRequest resultItem(Material type, int amount) { return resultItem(new ItemStack(type, amount)); }
        default SmithingTableUpdateRequest resultItem(Material type) { return resultItem(type, 1); }

    }

    private static final class BaseSmithingTableUpdateRequest extends BaseContentUpdateRequest<SmithingTableContainer, SmithingTableUpdateRequest> implements SmithingTableUpdateRequest {
        private BaseSmithingTableUpdateRequest(SmithingTableContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseSmithingTableUpdateRequest(SmithingTableContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public SmithingTableUpdateRequest itemToUpgrade(ItemStack item) {
            return set(item, ITEM_TO_UPGRADE_SLOT, true);
        }

        @Override
        public SmithingTableUpdateRequest netherite(ItemStack item) {
            return set(item, NETHERITE_SLOT, true);
        }

        @Override
        public SmithingTableUpdateRequest resultItem(ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
