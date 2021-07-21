package ru.soknight.packetinventoryapi.item.update;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.item.WrappedItemStack;
import ru.soknight.packetinventoryapi.packet.PacketAssistant;
import ru.soknight.packetinventoryapi.packet.server.PacketServerWindowItems;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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

    private MenuItem<?, ?> getFiller() {
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
    public R fromParsedData(Iterable<? extends MenuItem<?, ?>> items, boolean replace) {
        if(items != null)
            items.forEach(item -> {
                ItemStack bukkitItem = item.asBukkitItem();

                FillPatternType pattern = item.getFillPattern();
                if(pattern != null)
                    fillPattern(bukkitItem, pattern, replace);

                int[] slots = item.getSlots();
                if(slots == null || slots.length == 0)
                    return;

                for(int slot : slots)
                    set(bukkitItem, slot, replace);
            });
        return getThis();
    }

    @Override
    public R insert(MenuItem<?, ?> item, boolean replace) {
        ItemStack bukkitItem = item.asBukkitItem();

        FillPatternType pattern = item.getFillPattern();
        if(pattern != null)
            fillPattern(bukkitItem, pattern, replace);

        int[] slots = item.getSlots();
        if(slots != null && slots.length != 0)
            for(int slot : slots)
                set(bukkitItem, slot, replace);

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
    public R setAt(ItemStack item, int x, int y, boolean replace) {
        Validate.isTrue(x >= 0 && x <= 8, "'x' must be in the range [0;8]");
        Validate.isTrue(y >= 0, "'y' must be not negative number");

        int slot = slotsOffset + y * 9 + x;
        return set(item, slot, replace);
    }
    
    @Override
    public R fill(ItemStack item, IntRange slotRange, boolean replace) {
        return fill(item, slotRange.getMin(), slotRange.getMax(), replace);
    }

    @Override
    public R fill(ItemStack item, int sourceSlot, int destSlot, boolean replace) {
        Validate.isTrue(sourceSlot >= 0 && destSlot >= 0, "slots must be not negative numbers");
        Validate.isTrue(sourceSlot <= destSlot, "'sourceSlot' must be less than or equals 'destSlot'");
        
        IntStream.rangeClosed(sourceSlot, destSlot).forEach(slot -> set(item, slot, replace));
        return getThis();
    }

    @Override
    public R fillAt(ItemStack item, IntRange xRange, IntRange yRange, boolean replace) {
        return fillAt(item, xRange.getMin(), yRange.getMin(), xRange.getMax(), yRange.getMax(), replace);
    }
    
    @Override
    public R fillAt(ItemStack item, int fromX, int fromY, int toX, int toY, boolean replace) {
        Validate.isTrue(fromX >= 0 && fromX <= 8, "'fromX' must be in the range [0;8]");
        Validate.isTrue(toX >= 0 && toX <= 8, "'fromX' must be in the range [0;8]");
        Validate.isTrue(fromX <= toX, "'fromX' must be less than or equals 'toX'");
        
        Validate.isTrue(fromY >= 0, "'fromY' must be not negative number");
        Validate.isTrue(toY >= 0, "'toY' must be not negative number");
        Validate.isTrue(fromY <= toY, "'fromY' must be less than or equals 'toY'");
        
        for(int x = fromX; x <= toX; x++)
            for(int y = fromY; y <= toY; y++)
                setAt(item, x, y, replace);
        
        return getThis();
    }

    @Override
    public R fillTop(ItemStack item, boolean replace) {
        return fill(item, container.containerSlots(), replace);
    }

    @Override
    public R fillBottom(ItemStack item, boolean replace) {
        return fill(item, container.playerInventorySlots().getMin(), container.playerHotbarSlots().getMax(), replace);
    }

    @Override
    public R fillPlayerInventory(ItemStack item, boolean replace) {
        return fill(item, container.playerInventorySlots(), replace);
    }

    @Override
    public R fillPlayerHotbar(ItemStack item, boolean replace) {
        return fill(item, container.playerHotbarSlots(), replace);
    }

    @Override
    public R fillAll(ItemStack item, boolean replace) {
        return fill(item, slotsOffset, container.playerHotbarSlots().getMax(), replace);
    }

    @Override
    public R fillPattern(ItemStack item, FillPatternType pattern, boolean replace) {
        Validate.notNull(pattern, "'pattern' cannot be null!");

        pattern.fill(item, this);
        return getThis();
    }

    @Override
    public R row(ItemStack item, int row, boolean replace) {
        Validate.isTrue(row >= 0, "'row' must be not negative number");
        
        return fillAt(item, 0, row, 8, row, replace);
    }
    
    @Override
    public R row(ItemStack item, int sourceRow, int destRow, boolean replace) {
        Validate.isTrue(sourceRow >= 0 && destRow >= 0, "rows must be not negative numbers");
        Validate.isTrue(sourceRow <= destRow, "'sourceRow' must be less than or equals 'destRow'");
        
        return fillAt(item, 0, sourceRow, 8, destRow, replace);
    }
    
    @Override
    public R row(ItemStack item, IntRange rowRange, boolean replace) {
        return row(item, rowRange.getMin(), rowRange.getMax(), replace);
    }

    @Override
    public R rows(ItemStack item, boolean replace, int... rows) {
        if(rows != null && rows.length != 0)
            for(int row : rows)
                row(item, row, replace);

        return getThis();
    }

    @Override
    public R rows(ItemStack item, boolean replace, Iterable<Integer> rows) {
        Validate.notNull(rows, "'rows' cannot be null!");

        rows.forEach(row -> row(item, row, replace));
        return getThis();
    }

    @Override
    public R column(ItemStack item, int column, boolean replace) {
        Validate.isTrue(column >= 0 && column <= 8, "'column' must be in the range [0;8]");

        int maxRow = getMaxRow();
        return fillAt(item, column, 0, column, maxRow, replace);
    }
    
    @Override
    public R column(ItemStack item, int sourceColumn, int destColumn, boolean replace) {
        Validate.isTrue(sourceColumn >= 0 && sourceColumn <= 8, "'sourceColumn' must be in the range [0;8]");
        Validate.isTrue(destColumn >= 0 && destColumn <= 8, "'destColumn' must be in the range [0;8]");
        
        Validate.isTrue(sourceColumn >= 0 && destColumn >= 0, "columns must be not negative numbers");
        Validate.isTrue(sourceColumn <= destColumn, "'sourceColumn' must be less than or equals 'destColumn'");

        int maxRow = getMaxRow();
        return fillAt(item, sourceColumn, 0, destColumn, maxRow, replace);
    }

    @Override
    public R column(ItemStack item, IntRange columnRange, boolean replace) {
        return column(item, columnRange.getMin(), columnRange.getMax(), replace);
    }

    @Override
    public R columns(ItemStack item, boolean replace, int... columns) {
        if(columns != null && columns.length != 0)
            for(int column : columns)
                column(item, column, replace);

        return getThis();
    }

    @Override
    public R columns(ItemStack item, boolean replace, Iterable<Integer> columns) {
        Validate.notNull(columns, "'columns' cannot be null!");

        columns.forEach(column -> column(item, column, replace));
        return getThis();
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
        return fill(EMPTY_ITEM, sourceSlot, destSlot, true);
    }
    
    @Override
    public R removeAll(IntRange xRange, IntRange yRange) {
        return removeAll(xRange.getMin(), yRange.getMin(), xRange.getMax(), yRange.getMax());
    }

    @Override
    public R removeAll(int fromX, int fromY, int toX, int toY) {
        return fillAt(EMPTY_ITEM, fromX, fromY, toX, toY, true);
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

        MenuItem<?, ?> filler = getFiller();
        if(filler != null)
            insert(filler, false);

        itemMatrix.forEach(slotData::set);

        // replacing placeholders to real values
        slotData.replaceAll(this::replacePlaceholders);

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

    @SuppressWarnings("deprecation")
    protected ItemStack replacePlaceholders(ItemStack item) {
        if(item == null || !item.hasItemMeta())
            return item;

        ItemStack clone = item.clone();
        ItemMeta itemMeta = item.getItemMeta();

        if(itemMeta.hasDisplayName()) {
            String displayName = itemMeta.getDisplayName();
            itemMeta.setDisplayName(container.replacePlaceholders(displayName));
        }

        if(itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            itemMeta.setLore(container.replacePlaceholders(lore));
        }

        if(item instanceof WrappedItemStack) {
            WrappedItemStack wrapper = (WrappedItemStack) item;
            String playerHead = wrapper.getVanillaItem().getPlayerHead();
            if(playerHead != null && !playerHead.isEmpty() && itemMeta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                OfflinePlayer owningPlayer = Bukkit.getOfflinePlayer(container.replacePlaceholders(playerHead));
                skullMeta.setOwningPlayer(owningPlayer);
            }
        }

        clone.setItemMeta(itemMeta);
        return clone;
    }

}
