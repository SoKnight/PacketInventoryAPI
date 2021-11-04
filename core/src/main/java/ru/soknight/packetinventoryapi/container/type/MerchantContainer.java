package ru.soknight.packetinventoryapi.container.type;

import lombok.AccessLevel;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerTypes;
import ru.soknight.packetinventoryapi.container.data.VillagerLevel;
import ru.soknight.packetinventoryapi.event.container.TradeSelectEvent;
import ru.soknight.packetinventoryapi.item.update.MerchantUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.TradeSelectListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class MerchantContainer extends Container<MerchantContainer, MerchantContainer.MerchantContentUpdateRequest> {

    public static final int FIRST_INPUT_SLOT = 0;
    public static final int SECOND_INPUT_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    private final List<MerchantRecipe> tradeList;
    private @Nullable EventListener<TradeSelectEvent> tradeSelectListener;
    private int selectedSlot;

    private @Nullable VillagerLevel villagerLevel;
    private int villagerExp;
    private boolean wanderingTrader;

    @Getter(AccessLevel.NONE)
    private boolean canRestock;

    private MerchantContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.MERCHANT, title);
        this.tradeList = new ArrayList<>();
    }

    private MerchantContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.MERCHANT, title);
        this.tradeList = new ArrayList<>();
    }

    public static @NotNull MerchantContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new MerchantContainer(inventoryHolder, title);
    }

    public static @NotNull MerchantContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new MerchantContainer(inventoryHolder, title);
    }

    public static @NotNull MerchantContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.VILLAGER_NONE);
    }

    @Override
    protected @NotNull MerchantContainer getThis() {
        return this;
    }

    @Override
    public @NotNull MerchantContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull MerchantContentUpdateRequest updateContent() {
        return MerchantContentUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(@NotNull MerchantContainer clone, @NotNull AnyEventListener listener) {
        clone.tradeSelectListener = listener::handle;
    }

    public @NotNull @UnmodifiableView List<MerchantRecipe> getTradeList() {
        return Collections.unmodifiableList(tradeList);
    }

    public boolean canRestock() {
        return canRestock;
    }

    // --- trades list & merchant data updating
    public @NotNull MerchantUpdateRequest updateMerchant() {
        return MerchantUpdateRequest.create(this, tradeList);
    }

    public @NotNull MerchantContainer submitUpdate(@NotNull MerchantUpdateRequest request) {
        if(!request.isPushed())
            throw new IllegalArgumentException("'request' must be already pushed to submit it!");

        this.villagerLevel = request.getVillagerLevel();
        this.villagerExp = request.getVillagerExp();
        this.wanderingTrader = request.isWanderingTrader();
        this.canRestock = request.canRestock();
        return this;
    }

    /**********************
     *  Events listening  *
     *********************/

    // --- trade select listening
    public @NotNull MerchantContainer tradeSelectListener(@Nullable TradeSelectListener listener) {
        this.tradeSelectListener = listener;
        return this;
    }
    
    public void onTradeSelected(@NotNull TradeSelectEvent event) {
        this.selectedSlot = event.getSelectedSlot();
        
        if(tradeSelectListener != null)
            tradeSelectListener.handle(event);
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, 2);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(3, 29);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(30, 38);
    }

    public interface MerchantContentUpdateRequest extends ContentUpdateRequest<MerchantContainer, MerchantContentUpdateRequest> {

        static @NotNull MerchantContentUpdateRequest create(
                @NotNull MerchantContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseMerchantContentUpdateRequest(container, contentData);
        }

        static @NotNull MerchantContentUpdateRequest create(
                @NotNull MerchantContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseMerchantContentUpdateRequest(container, contentData, slotsOffset);
        }

        // --- first item slot
        @NotNull MerchantContentUpdateRequest firstItem(@Nullable ItemStack item);

        default @NotNull MerchantContentUpdateRequest firstItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return firstItem(new ItemStack(type, amount));
        }

        default @NotNull MerchantContentUpdateRequest firstItem(@NotNull Material type) {
            return firstItem(type, 1);
        }

        // --- second item slot
        @NotNull MerchantContentUpdateRequest secondItem(@Nullable ItemStack item);

        default @NotNull MerchantContentUpdateRequest secondItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return secondItem(new ItemStack(type, amount));
        }

        default @NotNull MerchantContentUpdateRequest secondItem(@NotNull Material type) {
            return secondItem(type, 1);
        }

        // --- result item slot
        @NotNull MerchantContentUpdateRequest resultItem(@Nullable ItemStack item);

        default @NotNull MerchantContentUpdateRequest resultItem(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return resultItem(new ItemStack(type, amount));
        }

        default @NotNull MerchantContentUpdateRequest resultItem(@NotNull Material type) {
            return resultItem(type, 1);
        }

    }

    private static final class BaseMerchantContentUpdateRequest extends BaseContentUpdateRequest<MerchantContainer, MerchantContentUpdateRequest> implements MerchantContentUpdateRequest {
        private BaseMerchantContentUpdateRequest(
                @NotNull MerchantContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseMerchantContentUpdateRequest(
                @NotNull MerchantContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull MerchantContentUpdateRequest firstItem(@Nullable ItemStack item) {
            return set(item, FIRST_INPUT_SLOT, true);
        }

        @Override
        public @NotNull MerchantContentUpdateRequest secondItem(@Nullable ItemStack item) {
            return set(item, SECOND_INPUT_SLOT, true);
        }

        @Override
        public @NotNull MerchantContentUpdateRequest resultItem(@Nullable ItemStack item) {
            return set(item, RESULT_SLOT, true);
        }
    }
    
}
