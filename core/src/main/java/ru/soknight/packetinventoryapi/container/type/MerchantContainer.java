package ru.soknight.packetinventoryapi.container.type;

import lombok.AccessLevel;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.data.VillagerLevel;
import ru.soknight.packetinventoryapi.event.container.TradeSelectEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.MerchantUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.TradeSelectListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class MerchantContainer extends Container<MerchantContainer, MerchantContainer.MerchantContentUpdateRequest> {

    public static final int FIRST_INPUT_SLOT = 0;
    public static final int SECOND_INPUT_SLOT = 1;
    public static final int RESULT = 2;

    private final List<MerchantRecipe> tradeList;
    private EventListener<TradeSelectEvent> tradeSelectListener;
    private int selectedSlot;

    private VillagerLevel villagerLevel;
    private int villagerExp;
    private boolean wanderingTrader;

    @Getter(AccessLevel.NONE)
    private boolean canRestock;

    private MerchantContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.MERCHANT, title);
        this.tradeList = new ArrayList<>();
    }

    private MerchantContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.MERCHANT, title);
        this.tradeList = new ArrayList<>();
    }

    public static MerchantContainer create(Player inventoryHolder, String title) {
        return new MerchantContainer(inventoryHolder, title);
    }

    public static MerchantContainer create(Player inventoryHolder, BaseComponent title) {
        return new MerchantContainer(inventoryHolder, title);
    }

    public static MerchantContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.VILLAGER_NONE);
    }

    @Override
    protected MerchantContainer getThis() {
        return this;
    }

    @Override
    public MerchantContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public MerchantContentUpdateRequest updateContent() {
        return MerchantContentUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(MerchantContainer clone, AnyEventListener listener) {
        clone.tradeSelectListener = listener::handle;
    }

    public List<MerchantRecipe> getTradeList() {
        return Collections.unmodifiableList(tradeList);
    }

    public boolean canRestock() {
        return canRestock;
    }

    // --- trades list & merchant data updating
    public MerchantUpdateRequest updateMerchant() {
        return MerchantUpdateRequest.create(this, tradeList);
    }

    public MerchantContainer submitUpdate(MerchantUpdateRequest request) {
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
    public MerchantContainer tradeSelectListener(TradeSelectListener listener) {
        this.tradeSelectListener = listener;
        return this;
    }
    
    public void onTradeSelected(TradeSelectEvent event) {
        this.selectedSlot = event.getSelectedSlot();
        
        if(tradeSelectListener != null)
            tradeSelectListener.handle(event);
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

    public interface MerchantContentUpdateRequest extends ContentUpdateRequest<MerchantContainer, MerchantContentUpdateRequest> {

        static MerchantContentUpdateRequest create(MerchantContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseMerchantContentUpdateRequest(container, contentData);
        }

        static MerchantContentUpdateRequest create(MerchantContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseMerchantContentUpdateRequest(container, contentData, slotsOffset);
        }

        MerchantContentUpdateRequest firstItem(ItemStack item);
        default MerchantContentUpdateRequest firstItem(Material type, int amount) { return firstItem(new ItemStack(type, amount)); }
        default MerchantContentUpdateRequest firstItem(Material type) { return firstItem(type, 1); }

        MerchantContentUpdateRequest secondItem(ItemStack item);
        default MerchantContentUpdateRequest secondItem(Material type, int amount) { return secondItem(new ItemStack(type, amount)); }
        default MerchantContentUpdateRequest secondItem(Material type) { return secondItem(type, 1); }

        MerchantContentUpdateRequest resultItem(ItemStack item);
        default MerchantContentUpdateRequest resultItem(Material type, int amount) { return resultItem(new ItemStack(type, amount)); }
        default MerchantContentUpdateRequest resultItem(Material type) { return resultItem(type, 1); }

    }

    private static final class BaseMerchantContentUpdateRequest extends BaseContentUpdateRequest<MerchantContainer, MerchantContentUpdateRequest> implements MerchantContentUpdateRequest {
        private BaseMerchantContentUpdateRequest(MerchantContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseMerchantContentUpdateRequest(MerchantContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public MerchantContentUpdateRequest firstItem(ItemStack item) {
            return set(item, FIRST_INPUT_SLOT, true);
        }

        @Override
        public MerchantContentUpdateRequest secondItem(ItemStack item) {
            return set(item, SECOND_INPUT_SLOT, true);
        }

        @Override
        public MerchantContentUpdateRequest resultItem(ItemStack item) {
            return set(item, RESULT, true);
        }
    }
    
}
