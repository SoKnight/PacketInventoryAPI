package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.event.container.BeaconEffectChangeEvent;
import ru.soknight.packetinventoryapi.item.update.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.BeaconEffectChangeListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

@Getter
public class BeaconContainer extends Container<BeaconContainer, BeaconContainer.BeaconUpdateRequest> {
    
    public static final int PAYMENT_ITEM_SLOT = 0;
    
    private EventListener<BeaconEffectChangeEvent> effectChangeListener;
    private PotionEffectType currentPrimaryEffect;
    private PotionEffectType currentSecondaryEffect;

    private BeaconContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.BEACON, title);
    }

    private BeaconContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.BEACON, title);
    }

    public static BeaconContainer create(Player inventoryHolder, String title) {
        return new BeaconContainer(inventoryHolder, title);
    }

    public static BeaconContainer create(Player inventoryHolder, BaseComponent title) {
        return new BeaconContainer(inventoryHolder, title);
    }

    public static BeaconContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.BEACON);
    }

    @Override
    protected BeaconContainer getThis() {
        return this;
    }

    @Override
    public BeaconContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public BeaconUpdateRequest updateContent() {
        return BeaconUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(BeaconContainer clone, AnyEventListener listener) {
        clone.effectChangeListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- effect changing listening
    public BeaconContainer effectChangeListener(BeaconEffectChangeListener listener) {
        this.effectChangeListener = listener;
        return this;
    }
    
    public void onEffectChanged(BeaconEffectChangeEvent event) {
        this.currentPrimaryEffect = event.getPrimaryEffect();
        this.currentSecondaryEffect = event.getSecondaryEffect();
        
        if(effectChangeListener != null)
            effectChangeListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public BeaconContainer updatePowerLevel(int value) {
        updateProperty(Property.Beacon.POWER_LEVEL, value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public BeaconContainer updateFirstPotionEffect(int value) {
        updateProperty(Property.Beacon.FIRST_POTION_EFFECT, value);
        this.currentPrimaryEffect = PotionEffectType.getById(value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public BeaconContainer updateFirstPotionEffect(PotionEffectType effectType) {
        return updateFirstPotionEffect(effectType.getId());
    }

    @SuppressWarnings("deprecation")
    public BeaconContainer updateSecondPotionEffect(int value) {
        updateProperty(Property.Beacon.SECOND_POTION_EFFECT, value);
        this.currentSecondaryEffect = PotionEffectType.getById(value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public BeaconContainer updateSecondPotionEffect(PotionEffectType effectType) {
        return updateSecondPotionEffect(effectType.getId());
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(1, 27);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(28, 36);
    }

    public interface BeaconUpdateRequest extends ContentUpdateRequest<BeaconContainer, BeaconContainer.BeaconUpdateRequest> {

        static BeaconUpdateRequest create(BeaconContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseBeaconUpdateRequest(container, contentData);
        }

        static BeaconUpdateRequest create(BeaconContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseBeaconUpdateRequest(container, contentData, slotsOffset);
        }

        BeaconUpdateRequest paymentItem(ItemStack item);
        default BeaconUpdateRequest paymentItem(Material type, int amount) { return paymentItem(new ItemStack(type, amount)); }
        default BeaconUpdateRequest paymentItem(Material type) { return paymentItem(type, 1); }

    }

    private static final class BaseBeaconUpdateRequest extends BaseContentUpdateRequest<BeaconContainer, BeaconUpdateRequest> implements BeaconUpdateRequest {
        private BaseBeaconUpdateRequest(BeaconContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseBeaconUpdateRequest(BeaconContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public BeaconUpdateRequest paymentItem(ItemStack item) {
            return set(item, PAYMENT_ITEM_SLOT, true);
        }
    }
    
}
