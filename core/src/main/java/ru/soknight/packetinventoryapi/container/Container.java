package ru.soknight.packetinventoryapi.container;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.animation.Animation;
import ru.soknight.packetinventoryapi.animation.AttachableAnimation;
import ru.soknight.packetinventoryapi.animation.function.AnimationCreator;
import ru.soknight.packetinventoryapi.animation.function.AttachableCreator;
import ru.soknight.packetinventoryapi.animation.function.CompletionTask;
import ru.soknight.packetinventoryapi.api.PacketInventoryAPI;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.event.window.*;
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
import ru.soknight.packetinventoryapi.util.Colorizer;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
public abstract class Container<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> implements CloneableContainer<C, R> {
    
    protected final Map<Integer, EventListener<WindowClickEvent<C, R>>> slotsClickListeners;
    protected final Map<IntRange, EventListener<WindowClickEvent<C, R>>> rangesClickListeners;

    protected final @NotNull Map<Integer, ItemStack> contentData;
    protected final int inventoryId;
    protected final @Nullable Player inventoryHolder;
    protected final @NotNull ContainerType containerType;
    protected final @NotNull DataHolder dataHolder;
    protected @NotNull PlaceholderContext placeholderContext;
    protected @NotNull BaseComponent title;
    protected @Nullable DisplayableMenuItem filler;
    
    protected @Nullable EventListener<WindowOpenEvent<C, R>> openListener;
    protected @Nullable EventListener<WindowContentLoadEvent<C, R>> contentLoadListener;
    protected @Nullable EventListener<WindowCloseEvent<C, R>> closeListener;
    protected @Nullable EventListener<WindowPostCloseEvent<C, R>> postCloseListener;
    protected @Nullable ExtraDataProvider<C, R> extraDataProvider;
    private boolean viewing;

    protected boolean viewingPlayerInventory;
    protected boolean viewingHotbarContent;
    protected boolean finishAnimationsOnClose = true;
    protected boolean updateInventoryOnClose = true;
    protected boolean closeable = true;
    protected boolean interactable;
    protected boolean clickOutsideToClose;
    
    protected Container(@Nullable Player inventoryHolder, @NotNull ContainerType containerType, @Nullable String title) {
        this(inventoryHolder, containerType, Colorizer.asComponent(title));
    }

    protected Container(@Nullable Player inventoryHolder, @NotNull ContainerType containerType, @NotNull BaseComponent title) {
        Validate.notNull(containerType, "containerType");
        Validate.notNull(title, "title");
        
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

    protected abstract @NotNull C getThis();

    protected abstract @NotNull C copy(@Nullable Player holder);

    protected void hookEventListener(@NotNull C clone, @NotNull AnyEventListener listener) {}

    @Override
    public C copyFull(@Nullable Player holder, @NotNull AnyEventListener listener) {
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
            clone.postCloseListener = listener::handle;

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

    public @NotNull @UnmodifiableView Map<Integer, ItemStack> getContentData() {
        return Collections.unmodifiableMap(contentData);
    }

    // --- animations
    public <A extends Animation<A>> @NotNull C playAnimationSync(@NotNull AnimationCreator<C, R, A> creator) {
        return playAnimationSync(creator, 0L);
    }

    public <A extends Animation<A>> @NotNull C playAnimationSync(@NotNull AnimationCreator<C, R, A> creator, long delay) {
        Validate.notNull(creator, "creator");

        A animation = creator.create(getThis());
        if(animation != null)
            animation.playSync(delay);
        return getThis();
    }

    public <A extends Animation<A>> @NotNull C playAnimationAsync(@NotNull AnimationCreator<C, R, A> creator) {
        return playAnimationAsync(creator, 0L);
    }

    public <A extends Animation<A>> @NotNull C playAnimationAsync(@NotNull AnimationCreator<C, R, A> creator, long delay) {
        return playAnimationAsync(creator, null, delay);
    }

    public <A extends Animation<A>> @NotNull C playAnimationAsync(@NotNull AnimationCreator<C, R, A> creator, @Nullable CompletionTask<A> onFinish) {
        return playAnimationAsync(creator, onFinish, 0L);
    }

    public <A extends Animation<A>> @NotNull C playAnimationAsync(@NotNull AnimationCreator<C, R, A> creator, @Nullable CompletionTask<A> onFinish, long delay) {
        Validate.notNull(creator, "creator");

        A animation = creator.create(getThis());
        if(animation != null)
            animation.playAsync(onFinish, delay);
        return getThis();
    }

    // --- attachable animations
    public <A extends AttachableAnimation<A, T>, T> @NotNull C playAnimationSync(
            @NotNull AttachableCreator<C, R, A, T> creator,
            @NotNull CompletableFuture<T> task
    ) {
        return playAnimationSync(creator, task, 0L);
    }

    public <A extends AttachableAnimation<A, T>, T> @NotNull C playAnimationSync(
            @NotNull AttachableCreator<C, R, A, T> creator,
            @NotNull CompletableFuture<T> task,
            long delay
    ) {
        Validate.notNull(creator, "creator");
        Validate.notNull(task, "task");

        A animation = creator.create(getThis(), task);
        if(animation != null)
            animation.playSync(delay);
        return getThis();
    }

    public <A extends AttachableAnimation<A, T>, T> @NotNull C playAnimationAsync(
            @NotNull AttachableCreator<C, R, A, T> creator,
            @NotNull CompletableFuture<T> task
    ) {
        return playAnimationAsync(creator, task, 0L);
    }

    public <A extends AttachableAnimation<A, T>, T> @NotNull C playAnimationAsync(
            @NotNull AttachableCreator<C, R, A, T> creator,
            @NotNull CompletableFuture<T> task,
            long delay
    ) {
        return playAnimationAsync(creator, task, null, delay);
    }

    public <A extends AttachableAnimation<A, T>, T> @NotNull C playAnimationAsync(
            @NotNull AttachableCreator<C, R, A, T> creator,
            @NotNull CompletableFuture<T> task,
            @Nullable CompletionTask<A> onFinish
    ) {
        return playAnimationAsync(creator, task, onFinish, 0L);
    }

    public <A extends AttachableAnimation<A, T>, T> @NotNull C playAnimationAsync(
            @NotNull AttachableCreator<C, R, A, T> creator,
            @NotNull CompletableFuture<T> task,
            @Nullable CompletionTask<A> onFinish,
            long delay
    ) {
        Validate.notNull(creator, "creator");
        Validate.notNull(task, "task");

        A animation = creator.create(getThis(), task);
        if(animation != null)
            animation.playAsync(onFinish, delay);
        return getThis();
    }

    // --- base container properties
    public @NotNull C setTitle(@NotNull BaseComponent title) {
        Validate.notNull(title, "title");
        this.title = title;
        return getThis();
    }

    public @NotNull C setFiller(@Nullable DisplayableMenuItem filler) {
        this.filler = filler;
        return getThis();
    }

    public @NotNull C setExtraDataProvider(@Nullable ExtraDataProvider<C, R> extraDataProvider) {
        this.extraDataProvider = extraDataProvider;
        return getThis();
    }

    public @NotNull C setViewingPlayerInventory(boolean value) {
        this.viewingPlayerInventory = value;
        return getThis();
    }

    public @NotNull C setViewingHotbarContent(boolean value) {
        this.viewingHotbarContent = value;
        return getThis();
    }

    public @NotNull C setFinishAnimationsOnClose(boolean value) {
        this.finishAnimationsOnClose = value;
        return getThis();
    }

    public @NotNull C setUpdateInventoryOnClose(boolean value) {
        this.updateInventoryOnClose = value;
        return getThis();
    }

    public @NotNull C setCloseable(boolean value) {
        this.closeable = value;
        return getThis();
    }

    public @NotNull C setInteractable(boolean value) {
        this.interactable = value;
        return getThis();
    }

    public @NotNull C setClickOutsideToClose(boolean value) {
        this.clickOutsideToClose = value;
        return getThis();
    }

    // --- content items updating
    public abstract @NotNull R updateContent();

    public @NotNull C submitUpdate(@NotNull R request) {
        if(!request.isPushed())
            throw new IllegalArgumentException("'request' must be already pushed to submit it!");

        this.viewingPlayerInventory = request.isViewingPlayerInventory();
        this.viewingHotbarContent = request.isViewingHotbarContent();
        return getThis();
    }

    // --- window reopening
    public @NotNull C reopen() {
        return close().open();
    }

    /**********************
     *  Events listening  *
     *********************/

    public @NotNull @UnmodifiableView Map<Integer, EventListener<WindowClickEvent<C, R>>> getSlotsClickListeners() {
        return Collections.unmodifiableMap(slotsClickListeners);
    }

    public @NotNull @UnmodifiableView Map<IntRange, EventListener<WindowClickEvent<C, R>>> getRangesClickListeners() {
        return Collections.unmodifiableMap(rangesClickListeners);
    }

    // --- open listening
    public @NotNull C open() {
        return open(false);
    }

    public synchronized @NotNull C open(boolean reopened) {
        if(!reopened && isViewing())
            close();

        PacketInventoryAPI apiInstance = PacketInventoryAPIPlugin.getApiInstance();
        SimpleContainerStorage storage = (SimpleContainerStorage) apiInstance.containerStorage();
        storage.open(this, reopened);
        return getThis();
    }

    public @NotNull C openListener(@Nullable WindowOpenListener<C, R> listener) {
        this.openListener = listener;
        return getThis();
    }
    
    public synchronized void onOpen(boolean reopened) {
        viewing = true;

        if(!reopened && openListener != null)
            openListener.handle(new WindowOpenEvent<>(inventoryHolder, getThis(), reopened));

        updateContent().pushSync();

        if(contentLoadListener != null)
            contentLoadListener.handle(new WindowContentLoadEvent<>(inventoryHolder, getThis()));
    }
    
    // --- close listening
    public synchronized @NotNull C close() {
        PacketInventoryAPI apiInstance = PacketInventoryAPIPlugin.getApiInstance();
        SimpleContainerStorage storage = (SimpleContainerStorage) apiInstance.containerStorage();
        storage.close(this, false);
        return getThis();
    }

    public @NotNull C closeListener(@Nullable WindowCloseListener<C, R> listener) {
        this.closeListener = listener;
        return getThis();
    }

    public boolean onClose() {
        return onClose(false);
    }
    
    public synchronized boolean onClose(boolean requestedByHolder) {
        // TODO add close performer to do other action instead of container reopen
        if(requestedByHolder && !closeable) {
            open(true);
            return false;
        }

        viewing = false;
        if(finishAnimationsOnClose)
            Animation.finishAllSync(this);

        if(closeListener != null)
            closeListener.handle(new WindowCloseEvent<>(inventoryHolder, getThis(), requestedByHolder));

        if(updateInventoryOnClose)
            inventoryHolder.updateInventory();

        return true;
    }

    public void onPostClose(boolean requestedByHolder, boolean closedActually) {
        if(postCloseListener != null)
            postCloseListener.handle(new WindowPostCloseEvent<>(inventoryHolder, getThis(), requestedByHolder, closedActually));
    }

    // --- click listening
    public @NotNull C clickListener(int slot, @NotNull WindowClickListener<C, R> listener) {
        Validate.notNull(listener, "listener");
        slotsClickListeners.put(slot, listener);
        return getThis();
    }
    
    public @NotNull C clickListener(@NotNull IntRange slots, @NotNull WindowClickListener<C, R> listener) {
        Validate.notNull(slots, "slots");
        Validate.notNull(listener, "listener");
        rangesClickListeners.put(slots, listener);
        return getThis();
    }
    
    public @NotNull C playerInventoryClickListener(@NotNull WindowClickListener<C, R> listener) {
        return clickListener(playerInventorySlots(), listener);
    }
    
    public @NotNull C playerHotbarClickListener(@NotNull WindowClickListener<C, R> listener) {
        return clickListener(playerInventorySlots(), listener);
    }

    public void onClick(@NotNull WindowClickEvent<C, R> event) {
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
    public abstract @NotNull IntRange containerSlots();
    
    /**
     * Gets range of player's inventory slots depending on the children container type
     * <br>
     * So, every container will have a different slots range
     * @return player's inventory slots range
     */
    public abstract @NotNull IntRange playerInventorySlots();
    
    /**
     * Gets range of player's hotbar slots depending on the children container type
     * <br>
     * So, every container will have a different slots range
     * @return player's hotbar slots range
     */
    public abstract @NotNull IntRange playerHotbarSlots();
    
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
            throw new IllegalArgumentException(String.format(
                    "Property %s required container type %s, but current is %s!",
                    propertyType, propertyContainerType, containerType
            ));

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
