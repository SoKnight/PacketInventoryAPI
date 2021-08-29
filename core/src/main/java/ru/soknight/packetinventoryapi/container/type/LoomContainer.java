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
import ru.soknight.packetinventoryapi.event.container.PatternSelectEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.PatternSelectListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

@Getter
public class LoomContainer extends Container<LoomContainer, LoomContainer.LoomUpdateRequest> {

    public static final int BANNER_SLOT = 0;
    public static final int DYE_SLOT = 1;
    public static final int PATTERN_SLOT = 2;
    public static final int RESULT_SLOT = 3;
    
    private EventListener<PatternSelectEvent> patternSelectListener;
    private int selectedPattern;

    private LoomContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.LOOM, title);
    }

    private LoomContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.LOOM, title);
    }

    public static LoomContainer create(Player inventoryHolder, String title) {
        return new LoomContainer(inventoryHolder, title);
    }

    public static LoomContainer create(Player inventoryHolder, BaseComponent title) {
        return new LoomContainer(inventoryHolder, title);
    }

    public static LoomContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.LOOM);
    }

    @Override
    protected LoomContainer getThis() {
        return this;
    }

    @Override
    public LoomContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public LoomUpdateRequest updateContent() {
        return LoomUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(LoomContainer clone, AnyEventListener listener) {
        clone.patternSelectListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- pattern select listening
    public LoomContainer patternSelectListener(PatternSelectListener listener) {
        this.patternSelectListener = listener;
        return this;
    }
    
    public void onPatternSelected(PatternSelectEvent event) {
        this.selectedPattern = event.getSlot();
        
        if(patternSelectListener != null)
            patternSelectListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public LoomContainer updateSelectedPattern(int value) {
        updateProperty(Property.Loom.SELECTED_PATTERN, value);
        this.selectedPattern = value;
        return this;
    }
    
    public LoomContainer updateSelectedPattern(int row, int column) {
        return updateSelectedPattern(4 * row + column);
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, 3);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(4, 30);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(31, 39);
    }

    public interface LoomUpdateRequest extends ContentUpdateRequest<LoomContainer, LoomUpdateRequest> {

        static LoomUpdateRequest create(LoomContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseLoomUpdateRequest(container, contentData);
        }

        static LoomUpdateRequest create(LoomContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseLoomUpdateRequest(container, contentData, slotsOffset);
        }

        LoomUpdateRequest banner(ItemStack item);
        default LoomUpdateRequest banner(Material type, int amount) { return banner(new ItemStack(type, amount)); }
        default LoomUpdateRequest banner(Material type) { return banner(type, 1); }

        LoomUpdateRequest dye(ItemStack item);
        default LoomUpdateRequest dye(Material type, int amount) { return dye(new ItemStack(type, amount)); }
        default LoomUpdateRequest dye(Material type) { return dye(type, 1); }

        LoomUpdateRequest pattern(ItemStack item);
        default LoomUpdateRequest pattern(Material type, int amount) { return pattern(new ItemStack(type, amount)); }
        default LoomUpdateRequest pattern(Material type) { return pattern(type, 1); }

        LoomUpdateRequest resultItem(ItemStack item);
        default LoomUpdateRequest resultItem(Material type, int amount) { return resultItem(new ItemStack(type, amount)); }
        default LoomUpdateRequest resultItem(Material type) { return resultItem(type, 1); }

    }

    private static final class BaseLoomUpdateRequest extends BaseContentUpdateRequest<LoomContainer, LoomUpdateRequest> implements LoomUpdateRequest {
        private BaseLoomUpdateRequest(LoomContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseLoomUpdateRequest(LoomContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public LoomUpdateRequest banner(ItemStack item) {
            return set(item, BANNER_SLOT, true);
        }

        @Override
        public LoomUpdateRequest dye(ItemStack item) {
            return set(item, DYE_SLOT, true);
        }

        @Override
        public LoomUpdateRequest pattern(ItemStack item) {
            return set(item, PATTERN_SLOT, true);
        }

        @Override
        public LoomUpdateRequest resultItem(ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
