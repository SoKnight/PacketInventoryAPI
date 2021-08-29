package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.data.EnchantmentPosition;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.event.container.EnchantmentSelectEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.EnchantmentSelectListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

@Getter
public class EnchantmentTableContainer extends Container<EnchantmentTableContainer, EnchantmentTableContainer.EnchantmentTableUpdateRequest> {

    public static final int ITEM_TO_ENCHANT_SLOT = 0;
    public static final int LAPIS_LAZULI_SLOT = 1;

    public static final int DEFAULT_SEED = 0xFFFFFFF0;
    
    private EventListener<EnchantmentSelectEvent> selectListener;

    private EnchantmentTableContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.ENCHANTMENT_TABLE, title);
    }

    private EnchantmentTableContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.ENCHANTMENT_TABLE, title);
    }

    public static EnchantmentTableContainer create(Player inventoryHolder, String title) {
        return new EnchantmentTableContainer(inventoryHolder, title);
    }

    public static EnchantmentTableContainer create(Player inventoryHolder, BaseComponent title) {
        return new EnchantmentTableContainer(inventoryHolder, title);
    }

    public static EnchantmentTableContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.ENCHANTMENT_TABLE);
    }

    @Override
    protected EnchantmentTableContainer getThis() {
        return this;
    }

    @Override
    public EnchantmentTableContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public EnchantmentTableUpdateRequest updateContent() {
        return EnchantmentTableUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(EnchantmentTableContainer clone, AnyEventListener listener) {
        clone.selectListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- enchantment select listening
    public EnchantmentTableContainer selectListener(EnchantmentSelectListener listener) {
        this.selectListener = listener;
        return this;
    }
    
    public void onEnchantmentSelected(EnchantmentSelectEvent event) {
        if(selectListener != null)
            selectListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public EnchantmentTableContainer updateSeed(int value) {
        updateProperty(Property.EnchantmentTable.ENCHANTMENT_SEED, value);
        return this;
    }
    
    public EnchantmentTableContainer updateLevelRequirement(EnchantmentPosition position, int value) {
        updateProperty(Property.EnchantmentTable.levelRequirement(position), value);
        return this;
    }
    
    public EnchantmentTableContainer updateEnchantmentId(EnchantmentPosition position, int value) {
        updateProperty(Property.EnchantmentTable.hoverEnchantmentId(position), value);
        return this;
    }
    
    public EnchantmentTableContainer updateEnchantmentLevel(EnchantmentPosition position, int value) {
        updateProperty(Property.EnchantmentTable.hoverEnchantmentLevel(position), value);
        return this;
    }
    
    public EnchantmentTableContainer updateEnchantment(
            EnchantmentPosition position,
            int enchantmentId,
            int enchantmentLevel
    ) {
        updateEnchantmentId(position, enchantmentId);
        updateEnchantmentLevel(position, enchantmentLevel);
        return this;
    }
    
    public EnchantmentTableContainer updateEnchantment(
            EnchantmentPosition position,
            int levelRequirement,
            int enchantmentId,
            int enchantmentLevel
    ) {
        updateLevelRequirement(position, levelRequirement);
        updateEnchantmentId(position, enchantmentId);
        updateEnchantmentLevel(position, enchantmentLevel);
        return this;
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

    public interface EnchantmentTableUpdateRequest extends ContentUpdateRequest<EnchantmentTableContainer, EnchantmentTableUpdateRequest> {

        static EnchantmentTableUpdateRequest create(EnchantmentTableContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseEnchantmentTableUpdateRequest(container, contentData);
        }

        static EnchantmentTableUpdateRequest create(EnchantmentTableContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseEnchantmentTableUpdateRequest(container, contentData, slotsOffset);
        }

        EnchantmentTableUpdateRequest itemToEnchant(ItemStack item);
        default EnchantmentTableUpdateRequest itemToEnchant(Material type, int amount) { return itemToEnchant(new ItemStack(type, amount)); }
        default EnchantmentTableUpdateRequest itemToEnchant(Material type) { return itemToEnchant(type, 1); }

        EnchantmentTableUpdateRequest lapisLazuli(ItemStack item);
        default EnchantmentTableUpdateRequest lapisLazuli(Material type, int amount) { return lapisLazuli(new ItemStack(type, amount)); }
        default EnchantmentTableUpdateRequest lapisLazuli(Material type) { return lapisLazuli(type, 1); }

    }

    private static final class BaseEnchantmentTableUpdateRequest extends BaseContentUpdateRequest<EnchantmentTableContainer, EnchantmentTableUpdateRequest> implements EnchantmentTableUpdateRequest {
        private BaseEnchantmentTableUpdateRequest(EnchantmentTableContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseEnchantmentTableUpdateRequest(EnchantmentTableContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public EnchantmentTableUpdateRequest itemToEnchant(ItemStack item) {
            return set(item, EnchantmentTableContainer.ITEM_TO_ENCHANT_SLOT, true);
        }

        @Override
        public EnchantmentTableUpdateRequest lapisLazuli(ItemStack item) {
            return set(item, EnchantmentTableContainer.LAPIS_LAZULI_SLOT, true);
        }
    }
    
}
