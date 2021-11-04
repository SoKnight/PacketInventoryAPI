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
import ru.soknight.packetinventoryapi.event.container.PatternSelectEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.PatternSelectListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

@Getter
public class LoomContainer extends Container<LoomContainer, LoomContainer.LoomUpdateRequest> {

    public static final int BANNER_SLOT = 0;
    public static final int DYE_SLOT = 1;
    public static final int PATTERN_SLOT = 2;
    public static final int RESULT_SLOT = 3;
    
    private @Nullable EventListener<PatternSelectEvent> patternSelectListener;
    private int selectedPattern;

    private LoomContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.LOOM, title);
    }

    private LoomContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.LOOM, title);
    }

    public static @NotNull LoomContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new LoomContainer(inventoryHolder, title);
    }

    public static @NotNull LoomContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new LoomContainer(inventoryHolder, title);
    }

    public static @NotNull LoomContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.LOOM);
    }

    @Override
    protected @NotNull LoomContainer getThis() {
        return this;
    }

    @Override
    public @NotNull LoomContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull LoomUpdateRequest updateContent() {
        return LoomUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(@NotNull LoomContainer clone, @NotNull AnyEventListener listener) {
        clone.patternSelectListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- pattern select listening
    public @NotNull LoomContainer patternSelectListener(@Nullable PatternSelectListener listener) {
        this.patternSelectListener = listener;
        return this;
    }
    
    public void onPatternSelected(@NotNull PatternSelectEvent event) {
        this.selectedPattern = event.getSlot();
        
        if(patternSelectListener != null)
            patternSelectListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public @NotNull LoomContainer updateSelectedPattern(int value) {
        updateProperty(Property.Loom.SELECTED_PATTERN, value);
        this.selectedPattern = value;
        return this;
    }
    
    public @NotNull LoomContainer updateSelectedPattern(int row, int column) {
        return updateSelectedPattern(4 * row + column);
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, 3);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(4, 30);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(31, 39);
    }

    public interface LoomUpdateRequest extends ContentUpdateRequest<LoomContainer, LoomUpdateRequest> {

        static @NotNull LoomUpdateRequest create(
                @NotNull LoomContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseLoomUpdateRequest(container, contentData);
        }

        static @NotNull LoomUpdateRequest create(
                @NotNull LoomContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseLoomUpdateRequest(container, contentData, slotsOffset);
        }

        // --- banner slot
        @NotNull LoomUpdateRequest banner(@Nullable ItemStack item);

        default @NotNull LoomUpdateRequest banner(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return banner(new ItemStack(type, amount));
        }

        default @NotNull LoomUpdateRequest banner(@NotNull Material type) {
            return banner(type, 1);
        }

        // --- dye slot
        @NotNull LoomUpdateRequest dye(@Nullable ItemStack item);

        default @NotNull LoomUpdateRequest dye(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return dye(new ItemStack(type, amount));
        }

        default @NotNull LoomUpdateRequest dye(@NotNull Material type) {
            return dye(type, 1);
        }

        // --- pattern slot
        @NotNull LoomUpdateRequest pattern(@Nullable ItemStack item);

        default @NotNull LoomUpdateRequest pattern(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return pattern(new ItemStack(type, amount));
        }

        default @NotNull LoomUpdateRequest pattern(@NotNull Material type) {
            return pattern(type, 1);
        }

        // --- result item slot
        @NotNull LoomUpdateRequest resultItem(@Nullable ItemStack item);

        default @NotNull LoomUpdateRequest resultItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return resultItem(new ItemStack(type, amount));
        }

        default @NotNull LoomUpdateRequest resultItem(@NotNull Material type) {
            return resultItem(type, 1);
        }

    }

    private static final class BaseLoomUpdateRequest extends BaseContentUpdateRequest<LoomContainer, LoomUpdateRequest> implements LoomUpdateRequest {
        private BaseLoomUpdateRequest(
                @NotNull LoomContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseLoomUpdateRequest(
                @NotNull LoomContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull LoomUpdateRequest banner(@Nullable ItemStack item) {
            return set(item, BANNER_SLOT, true);
        }

        @Override
        public @NotNull LoomUpdateRequest dye(@Nullable ItemStack item) {
            return set(item, DYE_SLOT, true);
        }

        @Override
        public @NotNull LoomUpdateRequest pattern(@Nullable ItemStack item) {
            return set(item, PATTERN_SLOT, true);
        }

        @Override
        public @NotNull LoomUpdateRequest resultItem(@Nullable ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
