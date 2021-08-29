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
import ru.soknight.packetinventoryapi.event.container.RecipeSelectEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.RecipeSelectListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

@Getter
public class StonecutterContainer extends Container<StonecutterContainer, StonecutterContainer.StonecutterUpdateRequest> {

    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    
    private EventListener<RecipeSelectEvent> recipeSelectListener;
    private int selectedRecipe;

    private StonecutterContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.STONECUTTER, title);
    }

    private StonecutterContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.STONECUTTER, title);
    }

    public static StonecutterContainer create(Player inventoryHolder, String title) {
        return new StonecutterContainer(inventoryHolder, title);
    }

    public static StonecutterContainer create(Player inventoryHolder, BaseComponent title) {
        return new StonecutterContainer(inventoryHolder, title);
    }

    public static StonecutterContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.STONECUTTER);
    }

    @Override
    protected StonecutterContainer getThis() {
        return this;
    }

    @Override
    public StonecutterContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public StonecutterUpdateRequest updateContent() {
        return StonecutterUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(StonecutterContainer clone, AnyEventListener listener) {
        clone.recipeSelectListener = listener::handle;
    }

    // TODO the stonecutter container must provide a way to provide needed
    // recipes in any time to the viewing player, so it's required a full
    // project rebuilding to the multi-module structure to support all new
    // NMS versions from 1.13.R1 to 1.17.R1 (is latest now)

    // also I need more information, how I can add custom serializer for
    // my recipes modifier and provide serialization of all types of recipes
    // which currently exists in the Bukkit API (including stonecutter recipes)

    /**********************
     *  Events listening  *
     *********************/

    // --- recipe select listening
    public StonecutterContainer recipeSelectListener(RecipeSelectListener listener) {
        this.recipeSelectListener = listener;
        return this;
    }
    
    public void onRecipeSelected(RecipeSelectEvent event) {
        this.selectedRecipe = event.getSlot();
        
        if(recipeSelectListener != null)
            recipeSelectListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public StonecutterContainer updateSelectedRecipe(int value) {
        updateProperty(Property.Stonecutter.SELECTED_RECIPE, value);
        this.selectedRecipe = value;
        return this;
    }
    
    public StonecutterContainer updateSelectedRecipe(int row, int column) {
        return updateSelectedRecipe(4 * row + column);
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, 1);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(2, 28);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(29, 37);
    }

    public interface StonecutterUpdateRequest extends ContentUpdateRequest<StonecutterContainer, StonecutterUpdateRequest> {

        static StonecutterUpdateRequest create(StonecutterContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseStonecutterUpdateRequest(container, contentData);
        }

        static StonecutterUpdateRequest create(StonecutterContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseStonecutterUpdateRequest(container, contentData, slotsOffset);
        }

        StonecutterUpdateRequest inputItem(ItemStack item);
        default StonecutterUpdateRequest inputItem(Material type, int amount) { return inputItem(new ItemStack(type, amount)); }
        default StonecutterUpdateRequest inputItem(Material type) { return inputItem(type, 1); }

        StonecutterUpdateRequest resultItem(ItemStack item);
        default StonecutterUpdateRequest resultItem(Material type, int amount) { return resultItem(new ItemStack(type, amount)); }
        default StonecutterUpdateRequest resultItem(Material type) { return resultItem(type, 1); }

    }

    private static final class BaseStonecutterUpdateRequest extends BaseContentUpdateRequest<StonecutterContainer, StonecutterUpdateRequest> implements StonecutterUpdateRequest {
        private BaseStonecutterUpdateRequest(StonecutterContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseStonecutterUpdateRequest(StonecutterContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public StonecutterUpdateRequest inputItem(ItemStack item) {
            return set(item, INPUT_SLOT, true);
        }

        @Override
        public StonecutterUpdateRequest resultItem(ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
