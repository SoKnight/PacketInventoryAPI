package ru.soknight.packetinventoryapi.container;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.animation.Animation;
import ru.soknight.packetinventoryapi.api.PacketInventoryAPI;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.event.window.WindowClickEvent;
import ru.soknight.packetinventoryapi.event.window.WindowCloseEvent;
import ru.soknight.packetinventoryapi.event.window.WindowContentLoadEvent;
import ru.soknight.packetinventoryapi.event.window.WindowOpenEvent;
import ru.soknight.packetinventoryapi.item.ExtraDataProvider;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.listener.event.window.WindowCloseListener;
import ru.soknight.packetinventoryapi.listener.event.window.WindowOpenListener;
import ru.soknight.packetinventoryapi.menu.container.CloneableContainer;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.packet.PacketAssistant;
import ru.soknight.packetinventoryapi.packet.server.PacketServerWindowProperty;
import ru.soknight.packetinventoryapi.placeholder.context.PlaceholderContext;
import ru.soknight.packetinventoryapi.storage.ContainerStorage;
import ru.soknight.packetinventoryapi.storage.SimpleContainerStorage;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public abstract class Container<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> implements CloneableContainer<C, R> {
    
    protected final Map<Integer, EventListener<WindowClickEvent<C, R>>> slotsClickListeners;
    protected final Map<IntRange, EventListener<WindowClickEvent<C, R>>> rangesClickListeners;

    protected final Map<Integer, ItemStack> contentData;
    protected final int inventoryId;
    protected final Player inventoryHolder;
    protected final ContainerType containerType;
    protected final DataHolder dataHolder;
    protected PlaceholderContext placeholderContext;
    protected BaseComponent title;
    protected DisplayableMenuItem filler;
    
    protected EventListener<WindowOpenEvent<C, R>> openListener;
    protected EventListener<WindowContentLoadEvent<C, R>> contentLoadListener;
    protected EventListener<WindowCloseEvent<C, R>> closeListener;
    protected ExtraDataProvider<C, R> extraDataProvider;
    private boolean viewing;

    protected boolean viewingPlayerInventory;
    protected boolean viewingHotbarContent;
    protected boolean finishAnimationsOnClose = true;
    protected boolean updateInventoryOnClose = true;
    protected boolean closeable = true;
    protected boolean interactable;
    protected boolean clickOutsideToClose;
    
    protected Container(Player inventoryHolder, ContainerType containerType, String title) {
        this(inventoryHolder, containerType, title != null ? new TextComponent(ChatColor.translateAlternateColorCodes('&', title)) : null);
    }

    protected Container(Player inventoryHolder, ContainerType containerType, BaseComponent title) {
        this.slotsClickListeners = new HashMap<>();
        this.rangesClickListeners = new HashMap<>();

        this.contentData = new LinkedHashMap<>();
        this.inventoryId = ContainerStorage.INVENTORY_ID;
        this.inventoryHolder = inventoryHolder;
        this.containerType = containerType;
        this.dataHolder = DataHolder.create();
        this.placeholderContext = PlaceholderContext.create(inventoryHolder);
        this.title = title;
    }

    protected abstract C getThis();

    protected abstract C copy(Player holder);

    protected void hookEventListener(C clone, AnyEventListener listener) {}

    @Override
    public C copyFull(Player holder, AnyEventListener listener) {
        C clone = copy(holder);

        clone.contentData.putAll(contentData);
        clone.placeholderContext = placeholderContext.duplicateFor(holder);

        clone.viewingPlayerInventory = viewingPlayerInventory;
        clone.viewingHotbarContent = viewingHotbarContent;
        clone.finishAnimationsOnClose = finishAnimationsOnClose;
        clone.updateInventoryOnClose = updateInventoryOnClose;
        clone.closeable = closeable;
        clone.interactable = interactable;
        clone.clickOutsideToClose = clickOutsideToClose;
        clone.filler = filler;
        clone.extraDataProvider = extraDataProvider;

        if(listener != null) {
            clone.openListener = listener::handle;
            clone.contentLoadListener = listener::handle;
            clone.closeListener = listener::handle;

            IntRange range = new IntRange(0, playerHotbarSlots().getMax());
            clone.rangesClickListeners.put(range, listener::handle);

            hookEventListener(clone, listener);
        }

        return clone;
    }

    public void sync(@NotNull Runnable task) {
        synchronized (this) {
            task.run();
        }
    }

    public Map<Integer, ItemStack> getContentData() {
        return Collections.unmodifiableMap(contentData);
    }

    // --- animations
    public <A extends Animation<A>> C playAnimationSync(Function<C, A> creator) {
        return playAnimationSync(creator, 0L);
    }

    public <A extends Animation<A>> C playAnimationSync(@NotNull Function<C, A> creator, long delay) {
        A animation = creator.apply(getThis());
        if(animation != null)
            animation.playSync(delay);
        return getThis();
    }

    public <A extends Animation<A>> C playAnimationAsync(Function<C, A> creator) {
        return playAnimationAsync(creator, 0L);
    }

    public <A extends Animation<A>> C playAnimationAsync(Function<C, A> creator, long delay) {
        return playAnimationAsync(creator, null, delay);
    }

    public <A extends Animation<A>> C playAnimationAsync(Function<C, A> creator, Consumer<A> onFinish) {
        return playAnimationAsync(creator, onFinish, 0L);
    }

    public <A extends Animation<A>> C playAnimationAsync(@NotNull Function<C, A> creator, Consumer<A> onFinish, long delay) {
        A animation = creator.apply(getThis());
        if(animation != null)
            animation.playAsync(onFinish, delay);
        return getThis();
    }

    // --- base container properties
    public C setTitle(BaseComponent title) {
        this.title = title;
        return getThis();
    }

    public C setFiller(DisplayableMenuItem filler) {
        this.filler = filler;
        return getThis();
    }

    public C setExtraDataProvider(ExtraDataProvider<C, R> extraDataProvider) {
        this.extraDataProvider = extraDataProvider;
        return getThis();
    }

    public C setViewingPlayerInventory(boolean value) {
        this.viewingPlayerInventory = value;
        return getThis();
    }

    public C setViewingHotbarContent(boolean value) {
        this.viewingHotbarContent = value;
        return getThis();
    }

    public C setFinishAnimationsOnClose(boolean value) {
        this.finishAnimationsOnClose = value;
        return getThis();
    }

    public C setUpdateInventoryOnClose(boolean value) {
        this.updateInventoryOnClose = value;
        return getThis();
    }

    public C setCloseable(boolean value) {
        this.closeable = value;
        return getThis();
    }

    public C setInteractable(boolean value) {
        this.interactable = value;
        return getThis();
    }

    public C setClickOutsideToClose(boolean value) {
        this.clickOutsideToClose = value;
        return getThis();
    }

    // --- content items updating
    public abstract R updateContent();

    public C submitUpdate(ContentUpdateRequest<C, R> request) {
        if(!request.isPushed())
            throw new IllegalArgumentException("'request' must be already pushed to submit it!");

        this.viewingPlayerInventory = request.isViewingPlayerInventory();
        this.viewingHotbarContent = request.isViewingHotbarContent();
        return getThis();
    }

    // --- window reopening
    public C reopen() {
        return close().open();
    }

    /**********************
     *  Events listening  *
     *********************/

    public Map<Integer, EventListener<WindowClickEvent<C, R>>> getSlotsClickListeners() {
        return Collections.unmodifiableMap(slotsClickListeners);
    }

    public Map<IntRange, EventListener<WindowClickEvent<C, R>>> getRangesClickListeners() {
        return Collections.unmodifiableMap(rangesClickListeners);
    }

    // --- open listening
    public C open() {
        return open(false);
    }

    public synchronized C open(boolean reopened) {
        if(!reopened && isViewing())
            close();

        PacketInventoryAPI apiInstance = PacketInventoryAPIPlugin.getApiInstance();
        SimpleContainerStorage storage = (SimpleContainerStorage) apiInstance.containerStorage();
        storage.open(this, reopened);
        return getThis();
    }

    public C openListener(WindowOpenListener<C, R> listener) {
        this.openListener = listener;
        return getThis();
    }
    
    public void onOpen(boolean reopened) {
        viewing = true;
        if(!reopened && openListener != null)
            openListener.handle(new WindowOpenEvent<>(inventoryHolder, getThis(), reopened));
        updateContent().pushSync();
        if(contentLoadListener != null)
            contentLoadListener.handle(new WindowContentLoadEvent<>(inventoryHolder, getThis()));
    }
    
    // --- close listening
    public synchronized C close() {
        PacketInventoryAPI apiInstance = PacketInventoryAPIPlugin.getApiInstance();
        SimpleContainerStorage storage = (SimpleContainerStorage) apiInstance.containerStorage();
        storage.close(this, false);
        return getThis();
    }

    public C closeListener(WindowCloseListener<C, R> listener) {
        this.closeListener = listener;
        return getThis();
    }

    public boolean onClose() {
        return onClose(false);
    }
    
    public boolean onClose(boolean closedByHolder) {
        if(closedByHolder && !closeable) {
            open(true);
            return false;
        }

        viewing = false;
        if(finishAnimationsOnClose)
            Animation.finishAllSync(this);

        if(closeListener != null)
            closeListener.handle(new WindowCloseEvent<>(inventoryHolder, getThis(), closedByHolder));

        if(updateInventoryOnClose)
            inventoryHolder.updateInventory();

        return true;
    }

    // --- click listening
    public C clickListener(int slot, WindowClickListener<C, R> listener) {
        slotsClickListeners.put(slot, listener);
        return getThis();
    }
    
    public C clickListener(IntRange slots, WindowClickListener<C, R> listener) {
        rangesClickListeners.put(slots, listener);
        return getThis();
    }
    
    public C playerInventoryClickListener(WindowClickListener<C, R> listener) {
        rangesClickListeners.put(playerInventorySlots(), listener);
        return getThis();
    }
    
    public C playerHotbarClickListener(WindowClickListener<C, R> listener) {
        rangesClickListeners.put(playerHotbarSlots(), listener);
        return getThis();
    }

    public void onClick(WindowClickEvent<C, R> event) {
        int slot = event.getClickedSlot();
        if(slotsClickListeners.containsKey(slot))
            slotsClickListeners.get(slot).handle(event);

        rangesClickListeners.entrySet()
                .parallelStream()
                .filter(e -> slot == -999 || e.getKey().contains(slot))
                .forEach(e -> e.getValue().handle(event));

        if(clickOutsideToClose && event.getClickType().isOutsideInventory())
            close();
    }

    /*********************
     *  Inventory slots  *
     ********************/

    /**
     * Gets range of container slots depending on the children container type
     * <br>
     * So, every container will have a different slots range
     * @return container slots range
     */
    public abstract IntRange containerSlots();
    
    /**
     * Gets range of player's inventory slots depending on the children container type
     * <br>
     * So, every container will have a different slots range
     * @return player's inventory slots range
     */
    public abstract IntRange playerInventorySlots();
    
    /**
     * Gets range of player's hotbar slots depending on the children container type
     * <br>
     * So, every container will have a different slots range
     * @return player's hotbar slots range
     */
    public abstract IntRange playerHotbarSlots();
    
    /**
     * Gets the player's inventory slots offset of first slot indexed as 0
     * <br>
     * Some inventories have custom clots (ex. Anvil has own 0-2 slots) and
     * player's inventory starts with other slot (not 0), in this case we need
     * to know about this slots offset to use some fill methods in the
     * {@link ContentUpdateRequest}
     */
    public int playerInventoryOffset() {
        return playerInventorySlots().getMin();
    }
    
    /**
     * Updates known inventory property with custom value
     * @param propertyType - known inventory property
     * @param value - new value for this property
     */
    public void updateProperty(@NotNull Property propertyType, int value) {
        ContainerType propertyContainerType = propertyType.getContainerType();
        if(propertyContainerType != containerType && !propertyContainerType.isFurnace())
            throw new IllegalStateException(
                    "this property requires container '" + propertyContainerType
                    + "', current container is '" + containerType + "'"
            );

        updatePropertyRaw(propertyType.getPropertyId(), value);
    }
    
    /**
     * Updates inventory property by raw property ID with custom value
     * <br>
     * See all known window properties <a href="https://wiki.vg/Protocol#Window_Property">here</a>.
     * @param propertyId - raw property ID
     * @param value - new value for this property
     */
    public void updatePropertyRaw(int propertyId, int value) {
        PacketAssistant.createServerPacket(PacketServerWindowProperty.class)
                .windowID(inventoryId)
                .property(propertyId)
                .value(value)
                .send(inventoryHolder);
    }
    
}
