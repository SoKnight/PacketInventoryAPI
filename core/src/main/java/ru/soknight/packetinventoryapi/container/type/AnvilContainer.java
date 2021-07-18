package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.event.container.AnvilRenameEvent;
import ru.soknight.packetinventoryapi.item.update.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.AnvilRenameListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

@Getter
public class AnvilContainer extends Container<AnvilContainer, AnvilContainer.AnvilUpdateRequest> {

    public static final int FIRST_ITEM_SLOT = 0;
    public static final int SECOND_ITEM_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    
    private EventListener<AnvilRenameEvent> itemRenameListener;
    private String customName;

    private AnvilContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.ANVIL, title);
    }

    private AnvilContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.ANVIL, title);
    }

    public static AnvilContainer create(Player inventoryHolder, String title) {
        return new AnvilContainer(inventoryHolder, title);
    }

    public static AnvilContainer create(Player inventoryHolder, BaseComponent title) {
        return new AnvilContainer(inventoryHolder, title);
    }

    public static AnvilContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.ANVIL);
    }

    @Override
    protected AnvilContainer getThis() {
        return this;
    }

    @Override
    public AnvilContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public AnvilUpdateRequest updateContent() {
        return AnvilUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(AnvilContainer clone, AnyEventListener listener) {
        clone.itemRenameListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- item renaming listening
    public AnvilContainer itemRenameListener(AnvilRenameListener listener) {
        this.itemRenameListener = listener;
        return this;
    }
    
    public void onItemRename(AnvilRenameEvent event) {
        this.customName = event.getCustomName();
        
        if(itemRenameListener != null)
            itemRenameListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public AnvilContainer updateRepairCost(int value) {
        updateProperty(Property.Anvil.REPAIR_COST, value);
        return this;
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

    public interface AnvilUpdateRequest extends ContentUpdateRequest<AnvilContainer, AnvilUpdateRequest> {

        static AnvilUpdateRequest create(AnvilContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseAnvilUpdateRequest(container, contentData);
        }

        static AnvilUpdateRequest create(AnvilContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseAnvilUpdateRequest(container, contentData, slotsOffset);
        }

        AnvilUpdateRequest firstItem(ItemStack item);
        default AnvilUpdateRequest firstItem(Material type, int amount) { return firstItem(new ItemStack(type, amount)); }
        default AnvilUpdateRequest firstItem(Material type) { return firstItem(type, 1); }

        AnvilUpdateRequest secondItem(ItemStack item);
        default AnvilUpdateRequest secondItem(Material type, int amount) { return secondItem(new ItemStack(type, amount)); }
        default AnvilUpdateRequest secondItem(Material type) { return secondItem(type, 1); }

        AnvilUpdateRequest resultItem(ItemStack item);
        default AnvilUpdateRequest resultItem(Material type, int amount) { return resultItem(new ItemStack(type, amount)); }
        default AnvilUpdateRequest resultItem(Material type) { return resultItem(type, 1); }

    }

    private static final class BaseAnvilUpdateRequest extends BaseContentUpdateRequest<AnvilContainer, AnvilUpdateRequest> implements AnvilUpdateRequest {
        private BaseAnvilUpdateRequest(AnvilContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseAnvilUpdateRequest(AnvilContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public AnvilUpdateRequest firstItem(ItemStack item) {
            return set(item, FIRST_ITEM_SLOT, true);
        }

        @Override
        public AnvilUpdateRequest secondItem(ItemStack item) {
            return set(item, SECOND_ITEM_SLOT, true);
        }

        @Override
        public AnvilUpdateRequest resultItem(ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }

}
