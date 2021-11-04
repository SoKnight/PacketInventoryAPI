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
import ru.soknight.packetinventoryapi.event.container.RecipeSelectEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.RecipeSelectListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

@Getter
public class StonecutterContainer extends Container<StonecutterContainer, StonecutterContainer.StonecutterUpdateRequest> {

    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    
    private @Nullable EventListener<RecipeSelectEvent> recipeSelectListener;
    private int selectedRecipe;

    private StonecutterContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.STONECUTTER, title);
    }

    private StonecutterContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.STONECUTTER, title);
    }

    public static @NotNull StonecutterContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new StonecutterContainer(inventoryHolder, title);
    }

    public static @NotNull StonecutterContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new StonecutterContainer(inventoryHolder, title);
    }

    public static @NotNull StonecutterContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.STONECUTTER);
    }

    @Override
    protected @NotNull StonecutterContainer getThis() {
        return this;
    }

    @Override
    public @NotNull StonecutterContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull StonecutterUpdateRequest updateContent() {
        return StonecutterUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(@NotNull StonecutterContainer clone, @NotNull AnyEventListener listener) {
        clone.recipeSelectListener = listener::handle;
    }

    // TODO the stonecutter container must provide a way to provide needed
    // recipes in any time to the viewing player, so it's required a full
    // project rebuilding to the multi-module structure to support all new
    // NMS versions from 1.13.R1 to 1.17.R1 (is latest now)

    // Also, I need more information, how I can add custom serializer for
    // my recipes modifier and provide serialization of all types of recipes
    // which currently exists in the Bukkit API (including stonecutter recipes)

    /**********************
     *  Events listening  *
     *********************/

    // --- recipe select listening
    public @NotNull StonecutterContainer recipeSelectListener(@Nullable RecipeSelectListener listener) {
        this.recipeSelectListener = listener;
        return this;
    }
    
    public void onRecipeSelected(@NotNull RecipeSelectEvent event) {
        this.selectedRecipe = event.getSlot();
        
        if(recipeSelectListener != null)
            recipeSelectListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public @NotNull StonecutterContainer updateSelectedRecipe(int value) {
        updateProperty(Property.Stonecutter.SELECTED_RECIPE, value);
        this.selectedRecipe = value;
        return this;
    }
    
    public @NotNull StonecutterContainer updateSelectedRecipe(int row, int column) {
        return updateSelectedRecipe(4 * row + column);
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, 1);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(2, 28);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(29, 37);
    }

    public interface StonecutterUpdateRequest extends ContentUpdateRequest<StonecutterContainer, StonecutterUpdateRequest> {

        static @NotNull StonecutterUpdateRequest create(
                @NotNull StonecutterContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseStonecutterUpdateRequest(container, contentData);
        }

        static @NotNull StonecutterUpdateRequest create(
                @NotNull StonecutterContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseStonecutterUpdateRequest(container, contentData, slotsOffset);
        }

        // --- input item slot
        @NotNull StonecutterUpdateRequest inputItem(@Nullable ItemStack item);

        default @NotNull StonecutterUpdateRequest inputItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return inputItem(new ItemStack(type, amount));
        }

        default @NotNull StonecutterUpdateRequest inputItem(@NotNull Material type) {
            return inputItem(type, 1);
        }

        // --- result item slot
        @NotNull StonecutterUpdateRequest resultItem(@Nullable ItemStack item);

        default @NotNull StonecutterUpdateRequest resultItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return resultItem(new ItemStack(type, amount));
        }

        default @NotNull StonecutterUpdateRequest resultItem(@NotNull Material type) {
            return resultItem(type, 1);
        }

    }

    private static final class BaseStonecutterUpdateRequest extends BaseContentUpdateRequest<StonecutterContainer, StonecutterUpdateRequest> implements StonecutterUpdateRequest {
        private BaseStonecutterUpdateRequest(
                @NotNull StonecutterContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseStonecutterUpdateRequest(
                @NotNull StonecutterContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull StonecutterUpdateRequest inputItem(@Nullable ItemStack item) {
            return set(item, INPUT_SLOT, true);
        }

        @Override
        public @NotNull StonecutterUpdateRequest resultItem(@Nullable ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
