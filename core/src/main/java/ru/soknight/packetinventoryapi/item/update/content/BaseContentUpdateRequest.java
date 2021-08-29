package ru.soknight.packetinventoryapi.item.update.content;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.exception.item.filler.InvalidColumnFillerException;
import ru.soknight.packetinventoryapi.exception.item.filler.InvalidFillerException;
import ru.soknight.packetinventoryapi.exception.item.filler.InvalidRowFillerException;
import ru.soknight.packetinventoryapi.item.ExtraDataProvider;
import ru.soknight.packetinventoryapi.item.update.content.filler.Filler;
import ru.soknight.packetinventoryapi.item.update.content.filler.column.ColumnFiller;
import ru.soknight.packetinventoryapi.item.update.content.filler.row.RowFiller;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;
import ru.soknight.packetinventoryapi.packet.PacketAssistant;
import ru.soknight.packetinventoryapi.packet.server.PacketServerWindowItems;
import ru.soknight.packetinventoryapi.placeholder.context.PlaceholderContext;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

@Getter
public class BaseContentUpdateRequest<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> implements ContentUpdateRequest<C, R> {
    
    private static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    private final C container;
    private final Map<Integer, ItemStack> itemMatrix;
    private final int slotsOffset;

    private boolean viewingPlayerInventory;
    private boolean viewingHotbarContent;

    private boolean includeInventorySlots;
    private boolean includeHotbarSlots;

    @Setter(AccessLevel.NONE)
    private boolean pushed;

    protected BaseContentUpdateRequest(C container, Map<Integer, ItemStack> contentData) {
        this(container, contentData, container.playerInventoryOffset());
    }
    
    protected BaseContentUpdateRequest(C container, Map<Integer, ItemStack> contentData, int slotsOffset) {
        this.container = container;
        this.itemMatrix = contentData;
        this.slotsOffset = slotsOffset;

        this.viewingPlayerInventory = container.isViewingPlayerInventory();
        this.viewingHotbarContent = container.isViewingHotbarContent();
    }
    
    @Override
    public C getContainer() {
        return container;
    }

    private DisplayableMenuItem getFiller() {
        return container.getFiller();
    }
    
    @SuppressWarnings("unchecked")
    R getThis() {
        return (R) this;
    }

    @Override
    public R setViewingPlayerInventory(boolean viewingPlayerInventory) {
        this.viewingPlayerInventory = viewingPlayerInventory;
        return getThis();
    }

    @Override
    public R setViewingHotbarContent(boolean viewingHotbarContent) {
        this.viewingHotbarContent = viewingHotbarContent;
        return getThis();
    }

    @Override
    public R resetItemMatrix() {
        itemMatrix.clear();
        return getThis();
    }

    @Override
    public boolean isSet(int slot) {
        return itemMatrix.containsKey(slot);
    }

    private int getMaxRow() {
        return (container.playerHotbarSlots().getMax() - slotsOffset) / 9;
    }

    @Override
    public R fromParsedData(Iterable<? extends MenuItem> items, boolean replace) {
        if(items != null)
            items.forEach(item -> {
                if(!(item instanceof RegularMenuItem<?, ?>))
                    return;

                RegularMenuItem<?, ?> menuItem = item.asRegularItem();
                insert(menuItem, replace);
            });

        return getThis();
    }

    @Override
    public R insert(DisplayableMenuItem item, boolean replace) {
        if(item == null)
            return getThis();

        fill(Filler.builder()
                .useForceReplace()
                .withMenuItem(item)
                .build()
        );

        return getThis();
    }

    @Override
    public R remove(DisplayableMenuItem item) {
        if(item == null)
            return getThis();

        int[] slots = item.getSlots();
        if(slots != null && slots.length != 0)
            for(int slot : slots)
                remove(slot);

        return getThis();
    }

    @Override
    public R set(ItemStack item, int slot, boolean replace) {
        Validate.isTrue(slot >= 0, "'slot' must be not negative number");

        if(replace || !isSet(slot))
            itemMatrix.put(slot, item);

        return getThis();
    }

    @Override
    public R set(IntFunction<ItemStack> itemProvider, int slot, boolean replace) {
        return set(itemProvider.apply(slot), slot, replace);
    }

    @Override
    public R setAt(ItemStack item, int x, int y, boolean replace) {
        return setAt(slot -> item, x, y, replace);
    }

    @Override
    public R setAt(@NotNull IntFunction<ItemStack> itemProvider, int x, int y, boolean replace) {
        Validate.isTrue(x >= 0 && x <= 8, "'x' must be in the range [0;8]");
        Validate.isTrue(y >= 0, "'y' must be not negative number");

        int slot = slotsOffset + y * 9 + x;
        return set(itemProvider.apply(slot), slot, replace);
    }

    @Override
    public R fill(@NotNull Filler filler) throws InvalidFillerException {
        Validate.notNull(filler, "filler");

        // --- filler validation
        if(!filler.hasItemStack() && !filler.hasMenuItem())
            throw new InvalidFillerException(filler, "filler instance must have any ItemStack or MenuItem!");

        if(!filler.hasSlotRange() && !filler.hasSlotRegion() && !filler.hasFillPattern())
            throw new InvalidFillerException(filler, "filler instance must have slot range/region or fill pattern!");

        // --- processing...
        Player viewer = container.getInventoryHolder();
        boolean forceReplace = filler.usesForceReplace();

        if(filler.hasItemStack()) {
            ItemStack itemStack = filler.getItemStack();
            fillUsing(filler, slot -> itemStack);
        }

        if(filler.hasMenuItem()) {
            DisplayableMenuItem menuItem = filler.getMenuItem();
            fillUsing(filler, slot -> menuItem.asBukkitItemFor(viewer, slot));
        }

        return getThis();
    }

    private void fillUsing(@NotNull Filler filler, @NotNull IntFunction<ItemStack> itemProvider) {
        boolean forceReplace = filler.usesForceReplace();

        if(filler.hasSlots()) {
            int[] slots = filler.getSlots();
            fillUsing(itemProvider, slots, forceReplace);
        }

        if(filler.hasSlotRange()) {
            int sourceSlot = filler.getSourceSlot().getAsInt();
            int destSlot = filler.getDestSlot().getAsInt();
            fillUsing(itemProvider, sourceSlot, destSlot, forceReplace);
        }

        if(filler.hasSlotRegion()) {
            int fromX = filler.getFromX().getAsInt();
            int fromY = filler.getFromY().getAsInt();
            int toX = filler.getToX().getAsInt();
            int toY = filler.getToY().getAsInt();
            fillUsing(itemProvider, fromX, fromY, toX, toY, forceReplace);
        }

        if(filler.hasFillPattern()) {
            FillPatternType fillPattern = filler.getFillPattern();
            switch (fillPattern) {
                case ALL:
                    fillUsing(itemProvider, slotsOffset, container.playerHotbarSlots().getMax(), forceReplace);
                    break;
                case TOP:
                    fillUsing(itemProvider, container.containerSlots(), forceReplace);
                    break;
                case BOTTOM:
                    fillUsing(itemProvider, container.playerInventorySlots().getMin(), container.playerHotbarSlots().getMax(), forceReplace);
                    break;
                case INVENTORY:
                    fillUsing(itemProvider, container.playerInventorySlots(), forceReplace);
                    break;
                case HOTBAR:
                    fillUsing(itemProvider, container.playerHotbarSlots(), forceReplace);
                    break;
                default:
                    throw new IllegalArgumentException("unexpected fill pattern type: " + fillPattern);
            }
        }
    }

    private void fillUsing(@NotNull IntFunction<ItemStack> itemProvider, @NotNull int @NotNull [] slots, boolean replace) {
        for(int slot : slots)
            set(itemProvider, slot, replace);
    }

    private void fillUsing(@NotNull IntFunction<ItemStack> itemProvider, @NotNull IntRange slotRange, boolean replace) {
        fillUsing(itemProvider, slotRange.getMin(), slotRange.getMax(), replace);
    }

    private void fillUsing(@NotNull IntFunction<ItemStack> itemProvider, int sourceSlot, int destSlot, boolean replace) {
        for(int slot = sourceSlot; slot <= destSlot; slot++)
            set(itemProvider, slot, replace);
    }

    private void fillUsing(@NotNull IntFunction<ItemStack> itemProvider, int fromX, int fromY, int toX, int toY, boolean replace) {
        for(int x = fromX; x <= toX; x++)
            for(int y = fromY; y <= toY; y++)
                setAt(itemProvider, x, y, replace);
    }

    @Override
    public R fillRow(@NotNull RowFiller rowFiller) throws InvalidRowFillerException {
        Validate.notNull(rowFiller, "rowFiller");

        // --- filler validation
        if(!rowFiller.hasItemStack() && !rowFiller.hasMenuItem())
            throw new InvalidRowFillerException(rowFiller, "filler instance must have any ItemStack or MenuItem!");

        if(!rowFiller.hasRows())
            throw new InvalidRowFillerException(rowFiller, "filler instance must have rows array!");

        // --- processing...
        Player viewer = container.getInventoryHolder();

        if(rowFiller.hasItemStack()) {
            ItemStack itemStack = rowFiller.getItemStack();
            fillRowUsing(rowFiller, slot -> itemStack);
        }

        if(rowFiller.hasMenuItem()) {
            DisplayableMenuItem menuItem = rowFiller.getMenuItem();
            fillRowUsing(rowFiller, slot -> menuItem.asBukkitItemFor(viewer, slot));
        }

        return getThis();
    }

    private void fillRowUsing(@NotNull RowFiller filler, @NotNull IntFunction<ItemStack> itemProvider) {
        int[] rows = filler.getRows();
        boolean forceReplace = filler.usesForceReplace();

        for(int row : rows)
            fillUsing(itemProvider, 0, row, 8, row, forceReplace);
    }

    @Override
    public R fillColumn(@NotNull ColumnFiller columnFiller) throws InvalidColumnFillerException {
        Validate.notNull(columnFiller, "columnFiller");

        // --- filler validation
        if(!columnFiller.hasItemStack() && !columnFiller.hasMenuItem())
            throw new InvalidColumnFillerException(columnFiller, "filler instance must have any ItemStack or MenuItem!");

        if(!columnFiller.hasColumns())
            throw new InvalidColumnFillerException(columnFiller, "filler instance must have columns array!");

        // --- processing...
        Player viewer = container.getInventoryHolder();

        if(columnFiller.hasItemStack()) {
            ItemStack itemStack = columnFiller.getItemStack();
            fillColumnUsing(columnFiller, slot -> itemStack);
        }

        if(columnFiller.hasMenuItem()) {
            DisplayableMenuItem menuItem = columnFiller.getMenuItem();
            fillColumnUsing(columnFiller, slot -> menuItem.asBukkitItemFor(viewer, slot));
        }

        return getThis();
    }

    private void fillColumnUsing(@NotNull ColumnFiller filler, @NotNull IntFunction<ItemStack> itemProvider) {
        int[] columns = filler.getColumns();
        boolean forceReplace = filler.usesForceReplace();

        int maxRow = getMaxRow();
        for(int column : columns)
            fillUsing(itemProvider, column, 0, column, maxRow, forceReplace);
    }

    @Override
    public R remove(int slot) {
        return set(EMPTY_ITEM, slot, true);
    }

    @Override
    public R remove(int x, int y) {
        return setAt(EMPTY_ITEM, x, y, true);
    }

    @Override
    public R removeAll() {
        return removeAll(0, container.playerHotbarSlots().getMax());
    }
    
    @Override
    public R removeAll(IntRange slotRange) {
        return removeAll(slotRange.getMin(), slotRange.getMax());
    }

    @Override
    public R removeAll(int sourceSlot, int destSlot) {
        fillUsing(slot -> EMPTY_ITEM, sourceSlot, destSlot, true);
        return getThis();
    }
    
    @Override
    public R removeAll(IntRange xRange, IntRange yRange) {
        return removeAll(xRange.getMin(), yRange.getMin(), xRange.getMax(), yRange.getMax());
    }

    @Override
    public R removeAll(int fromX, int fromY, int toX, int toY) {
        fillUsing(slot -> EMPTY_ITEM, fromX, fromY, toX, toY, true);
        return getThis();
    }

    @Override
    public C pushSync() {
        if(itemMatrix.isEmpty() || !container.isViewing())
            return container;
        
        int max = container.playerHotbarSlots().getMax() + 1;
        List<ItemStack> emptyList = Collections.nCopies(max, EMPTY_ITEM);
        List<ItemStack> slotData = new ArrayList<>(emptyList);

        Player holder = container.getInventoryHolder();
        if(holder == null || !holder.isOnline()) {
            this.pushed = true;
            return container;
        }

        PlayerInventory inventory = holder.getInventory();
        ItemStack[] content = inventory.getStorageContents();

        viewPlayerInventory(slotData, content);
        viewHotbarContent(slotData, content);

        DisplayableMenuItem filler = getFiller();
        if(filler != null)
            insert(filler, false);

        ExtraDataProvider<C, R> extraDataProvider = container.getExtraDataProvider();
        if(extraDataProvider != null)
            extraDataProvider.provideExtraData(this);

        itemMatrix.forEach(slotData::set);

        // replacing placeholders to real values
        PlaceholderContext context = container.getPlaceholderContext();
        for(int slot = 0; slot < slotData.size(); slot++)
            slotData.set(slot, context.replacePlaceholders(slotData.get(slot), slot));

        PacketAssistant.createServerPacket(PacketServerWindowItems.class)
                .windowID(container.getInventoryId())
                .items(slotData)
                .send(holder);

        this.pushed = true;
        return container;
    }

    @Override
    public CompletableFuture<C> pushAsync() {
        return CompletableFuture.supplyAsync(this::pushSync);
    }
    
    @Override
    public CompletableFuture<C> pushLater(long duration, TimeUnit timeUnit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(timeUnit.toMillis(duration));
                return pushSync();
            } catch (InterruptedException ignored) {
                return container;
            }
        });
    }

    private void viewPlayerInventory(List<ItemStack> slotData, ItemStack[] content) {
        if(!viewingPlayerInventory) return;

        ItemStack[] inventory = Arrays.copyOfRange(content, 9, 36);
        int firstIndex = container.playerInventorySlots().getMin();

        for(int i = 0; i < 27; i++) {
            int index = firstIndex + i;
            ItemStack item = inventory[i];
            if(item != null && !item.getType().isAir() && slotData.get(index) == EMPTY_ITEM)
                slotData.set(index, item);
        }
    }

    private void viewHotbarContent(List<ItemStack> slotData, ItemStack[] content) {
        if(!viewingHotbarContent) return;

        ItemStack[] hotbar = Arrays.copyOfRange(content, 0, 9);
        int firstIndex = container.playerHotbarSlots().getMin();

        for(int i = 0; i < 9; i++) {
            int index = firstIndex + i;
            ItemStack item = hotbar[i];
            if(item != null && !item.getType().isAir() && slotData.get(index) == EMPTY_ITEM)
                slotData.set(index, item);
        }
    }

}
