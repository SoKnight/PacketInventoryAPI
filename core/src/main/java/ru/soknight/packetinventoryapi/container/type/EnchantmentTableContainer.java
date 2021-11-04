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
import ru.soknight.packetinventoryapi.container.data.EnchantmentPosition;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.event.container.EnchantmentSelectEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.EnchantmentSelectListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

@Getter
public class EnchantmentTableContainer extends Container<EnchantmentTableContainer, EnchantmentTableContainer.EnchantmentTableUpdateRequest> {

    public static final int ITEM_TO_ENCHANT_SLOT = 0;
    public static final int LAPIS_LAZULI_SLOT = 1;

    public static final int DEFAULT_SEED = 0xFFFFFFF0;
    
    private @Nullable EventListener<EnchantmentSelectEvent> selectListener;

    private EnchantmentTableContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.ENCHANTMENT_TABLE, title);
    }

    private EnchantmentTableContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.ENCHANTMENT_TABLE, title);
    }

    public static @NotNull EnchantmentTableContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new EnchantmentTableContainer(inventoryHolder, title);
    }

    public static @NotNull EnchantmentTableContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new EnchantmentTableContainer(inventoryHolder, title);
    }

    public static @NotNull EnchantmentTableContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.ENCHANTMENT_TABLE);
    }

    @Override
    protected @NotNull EnchantmentTableContainer getThis() {
        return this;
    }

    @Override
    public @NotNull EnchantmentTableContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull EnchantmentTableUpdateRequest updateContent() {
        return EnchantmentTableUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(@NotNull EnchantmentTableContainer clone, @NotNull AnyEventListener listener) {
        clone.selectListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- enchantment select listening
    public @NotNull EnchantmentTableContainer selectListener(@Nullable EnchantmentSelectListener listener) {
        this.selectListener = listener;
        return this;
    }
    
    public void onEnchantmentSelected(@NotNull EnchantmentSelectEvent event) {
        if(selectListener != null)
            selectListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public @NotNull EnchantmentTableContainer updateSeed(int value) {
        updateProperty(Property.EnchantmentTable.ENCHANTMENT_SEED, value);
        return this;
    }
    
    public @NotNull EnchantmentTableContainer updateLevelRequirement(@NotNull EnchantmentPosition position, int value) {
        Validate.notNull(position, "position");
        updateProperty(Property.EnchantmentTable.levelRequirement(position), value);
        return this;
    }
    
    public @NotNull EnchantmentTableContainer updateEnchantmentId(@NotNull EnchantmentPosition position, int value) {
        Validate.notNull(position, "position");
        updateProperty(Property.EnchantmentTable.hoverEnchantmentId(position), value);
        return this;
    }
    
    public @NotNull EnchantmentTableContainer updateEnchantmentLevel(@NotNull EnchantmentPosition position, int value) {
        Validate.notNull(position, "position");
        updateProperty(Property.EnchantmentTable.hoverEnchantmentLevel(position), value);
        return this;
    }
    
    public @NotNull EnchantmentTableContainer updateEnchantment(
            @NotNull EnchantmentPosition position,
            int enchantmentId,
            int enchantmentLevel
    ) {
        Validate.notNull(position, "position");
        updateEnchantmentId(position, enchantmentId);
        updateEnchantmentLevel(position, enchantmentLevel);
        return this;
    }
    
    public @NotNull EnchantmentTableContainer updateEnchantment(
            @NotNull EnchantmentPosition position,
            int levelRequirement,
            int enchantmentId,
            int enchantmentLevel
    ) {
        Validate.notNull(position, "position");
        updateLevelRequirement(position, levelRequirement);
        updateEnchantmentId(position, enchantmentId);
        updateEnchantmentLevel(position, enchantmentLevel);
        return this;
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

    public interface EnchantmentTableUpdateRequest extends ContentUpdateRequest<EnchantmentTableContainer, EnchantmentTableUpdateRequest> {

        static @NotNull EnchantmentTableUpdateRequest create(
                @NotNull EnchantmentTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseEnchantmentTableUpdateRequest(container, contentData);
        }

        static @NotNull EnchantmentTableUpdateRequest create(
                @NotNull EnchantmentTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseEnchantmentTableUpdateRequest(container, contentData, slotsOffset);
        }

        // --- item to enchant slot
        @NotNull EnchantmentTableUpdateRequest itemToEnchant(@Nullable ItemStack item);

        default @NotNull EnchantmentTableUpdateRequest itemToEnchant(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return itemToEnchant(new ItemStack(type, amount));
        }

        default @NotNull EnchantmentTableUpdateRequest itemToEnchant(@NotNull Material type) {
            return itemToEnchant(type, 1);
        }

        // --- lapis lazuli slot
        @NotNull EnchantmentTableUpdateRequest lapisLazuli(@Nullable ItemStack item);

        default @NotNull EnchantmentTableUpdateRequest lapisLazuli(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return lapisLazuli(new ItemStack(type, amount));
        }

        default @NotNull EnchantmentTableUpdateRequest lapisLazuli(@NotNull Material type) {
            return lapisLazuli(type, 1);
        }

    }

    private static final class BaseEnchantmentTableUpdateRequest extends BaseContentUpdateRequest<EnchantmentTableContainer, EnchantmentTableUpdateRequest> implements EnchantmentTableUpdateRequest {
        private BaseEnchantmentTableUpdateRequest(
                @NotNull EnchantmentTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseEnchantmentTableUpdateRequest(
                @NotNull EnchantmentTableContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull EnchantmentTableUpdateRequest itemToEnchant(@Nullable ItemStack item) {
            return set(item, EnchantmentTableContainer.ITEM_TO_ENCHANT_SLOT, true);
        }

        @Override
        public @NotNull EnchantmentTableUpdateRequest lapisLazuli(@Nullable ItemStack item) {
            return set(item, EnchantmentTableContainer.LAPIS_LAZULI_SLOT, true);
        }
    }
    
}
