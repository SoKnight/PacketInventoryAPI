package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerTypes;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.event.container.BeaconEffectChangeEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.BeaconEffectChangeListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

@Getter
public class BeaconContainer extends Container<BeaconContainer, BeaconContainer.BeaconUpdateRequest> {
    
    public static final int PAYMENT_ITEM_SLOT = 0;
    
    private @Nullable EventListener<BeaconEffectChangeEvent> effectChangeListener;
    private @Nullable PotionEffectType currentPrimaryEffect;
    private @Nullable PotionEffectType currentSecondaryEffect;

    private BeaconContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.BEACON, title);
    }

    private BeaconContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.BEACON, title);
    }

    public static @NotNull BeaconContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new BeaconContainer(inventoryHolder, title);
    }

    public static @NotNull BeaconContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new BeaconContainer(inventoryHolder, title);
    }

    public static @NotNull BeaconContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.BEACON);
    }

    @Override
    protected @NotNull BeaconContainer getThis() {
        return this;
    }

    @Override
    public @NotNull BeaconContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull BeaconUpdateRequest updateContent() {
        return BeaconUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(@NotNull BeaconContainer clone, @NotNull AnyEventListener listener) {
        clone.effectChangeListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- effect changing listening
    public @NotNull BeaconContainer effectChangeListener(@Nullable BeaconEffectChangeListener listener) {
        this.effectChangeListener = listener;
        return this;
    }
    
    public void onEffectChanged(@NotNull BeaconEffectChangeEvent event) {
        this.currentPrimaryEffect = event.getPrimaryEffect();
        this.currentSecondaryEffect = event.getSecondaryEffect();
        
        if(effectChangeListener != null)
            effectChangeListener.handle(event);
    }
    
    /**************************
     *  Container properties  *
     *************************/
    
    public @NotNull BeaconContainer updatePowerLevel(int value) {
        updateProperty(Property.Beacon.POWER_LEVEL, value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public @NotNull BeaconContainer updateFirstPotionEffect(int value) {
        updateProperty(Property.Beacon.FIRST_POTION_EFFECT, value);
        this.currentPrimaryEffect = PotionEffectType.getById(value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public @NotNull BeaconContainer updateFirstPotionEffect(@NotNull PotionEffectType effectType) {
        return updateFirstPotionEffect(effectType.getId());
    }

    @SuppressWarnings("deprecation")
    public @NotNull BeaconContainer updateSecondPotionEffect(int value) {
        updateProperty(Property.Beacon.SECOND_POTION_EFFECT, value);
        this.currentSecondaryEffect = PotionEffectType.getById(value);
        return this;
    }

    @SuppressWarnings("deprecation")
    public @NotNull BeaconContainer updateSecondPotionEffect(@NotNull PotionEffectType effectType) {
        return updateSecondPotionEffect(effectType.getId());
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(1, 27);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(28, 36);
    }

    public interface BeaconUpdateRequest extends ContentUpdateRequest<BeaconContainer, BeaconContainer.BeaconUpdateRequest> {

        static @NotNull BeaconUpdateRequest create(
                @NotNull BeaconContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseBeaconUpdateRequest(container, contentData);
        }

        static @NotNull BeaconUpdateRequest create(
                @NotNull BeaconContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseBeaconUpdateRequest(container, contentData, slotsOffset);
        }

        // --- payment item slot
        @NotNull BeaconUpdateRequest paymentItem(@Nullable ItemStack item);

        default @NotNull BeaconUpdateRequest paymentItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return paymentItem(new ItemStack(type, amount));
        }

        default @NotNull BeaconUpdateRequest paymentItem(@NotNull Material type) {
            return paymentItem(type, 1);
        }

    }

    private static final class BaseBeaconUpdateRequest extends BaseContentUpdateRequest<BeaconContainer, BeaconUpdateRequest> implements BeaconUpdateRequest {
        private BaseBeaconUpdateRequest(
                @NotNull BeaconContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseBeaconUpdateRequest(
                @NotNull BeaconContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull BeaconUpdateRequest paymentItem(@Nullable ItemStack item) {
            return set(item, PAYMENT_ITEM_SLOT, true);
        }
    }
    
}
