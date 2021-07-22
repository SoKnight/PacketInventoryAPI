package ru.soknight.packetinventoryapi.item.update;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataBundle;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.item.RegularMenuItem;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface ContentUpdateRequest<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> R create(C container, Map<Integer, ItemStack> contentData) {
        return new BaseContentUpdateRequest<>(container, contentData).getThis();
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> R create(C container, Map<Integer, ItemStack> contentData, int slotsOffset) {
        return new BaseContentUpdateRequest<>(container, contentData, slotsOffset).getThis();
    }

    C getContainer();

    boolean isPushed();

    boolean isViewingPlayerInventory();
    R setViewingPlayerInventory(boolean value);

    default R doViewPlayerInventory() { return setViewingPlayerInventory(true); }
    default R doHidePlayerInventory() { return setViewingPlayerInventory(false); }

    boolean isViewingHotbarContent();
    R setViewingHotbarContent(boolean value);

    default R doViewHotbarContent() { return setViewingHotbarContent(true); }
    default R doHideHotbarContent() { return setViewingHotbarContent(false); }

    Map<Integer, ItemStack> getItemMatrix();
    R resetItemMatrix();

    boolean isSet(int slot);

    // --- using parsed data bundle
    R fromParsedData(Iterable<? extends MenuItem> items, boolean replace);
    default R fromParsedData(ParsedDataBundle parsedDataBundle, boolean replace) { return fromParsedData(parsedDataBundle.getContent().values(), replace); }

    // --- menu item insertion
    R insert(RegularMenuItem<?, ?> item, boolean replace);

    // --- setters
    R set(ItemStack item, int slot, boolean replace);
    default R set(Material type, int amount, int slot, boolean replace) { return set(new ItemStack(type, amount), slot, replace); }

    R setAt(ItemStack item, int x, int y, boolean replace);
    default R setAt(Material type, int amount, int x, int y, boolean replace) { return setAt(new ItemStack(type, amount), x, y, replace); }
    
    // --- fillers
    R fill(ItemStack item, IntRange slotRange, boolean replace);
    default R fill(Material type, int amount, IntRange slotRange, boolean replace) { return fill(new ItemStack(type, amount), slotRange, replace); }

    R fill(ItemStack item, int sourceSlot, int destSlot, boolean replace);
    default R fill(Material type, int amount, int sourceSlot, int destSlot, boolean replace) { return fill(new ItemStack(type, amount), sourceSlot, destSlot, replace); }
    
    R fillAt(ItemStack item, IntRange xRange, IntRange yRange, boolean replace);
    default R fillAt(Material type, int amount, IntRange xRange, IntRange yRange, boolean replace) { return fillAt(new ItemStack(type, amount), xRange, yRange, replace); }

    R fillAt(ItemStack item, int fromX, int fromY, int toX, int toY, boolean replace);
    default R fillAt(Material type, int amount, int fromX, int fromY, int toX, int toY, boolean replace) { return fillAt(new ItemStack(type, amount), fromX, fromY, toX, toY, replace); }

    R fillTop(ItemStack item, boolean replace);
    default R fillTop(Material type, int amount, boolean replace) { return fillTop(new ItemStack(type, amount), replace); }

    R fillBottom(ItemStack item, boolean replace);
    default R fillBottom(Material type, int amount, boolean replace) { return fillBottom(new ItemStack(type, amount), replace); }

    R fillPlayerInventory(ItemStack item, boolean replace);
    default R fillPlayerInventory(Material type, int amount, boolean replace) { return fillPlayerInventory(new ItemStack(type, amount), replace); }

    R fillPlayerHotbar(ItemStack item, boolean replace);
    default R fillPlayerHotbar(Material type, int amount, boolean replace) { return fillPlayerHotbar(new ItemStack(type, amount), replace); }

    R fillAll(ItemStack item, boolean replace);
    default R fillAll(Material type, int amount, boolean replace) { return fillAll(new ItemStack(type, amount), replace); }

    R fillPattern(ItemStack item, FillPatternType pattern, boolean replace);
    default R fillPattern(Material type, int amount, FillPatternType pattern, boolean replace) { return fillPattern(new ItemStack(type, amount), pattern, replace); }

    // --- row fillers
    R row(ItemStack item, int row, boolean replace);
    default R row(Material type, int amount, int row, boolean replace) { return row(new ItemStack(type, amount), row, replace); }
    
    R row(ItemStack item, int sourceRow, int destRow, boolean replace);
    default R row(Material type, int amount, int sourceRow, int destRow, boolean replace) { return row(new ItemStack(type, amount), sourceRow, destRow, replace); }
    
    R row(ItemStack item, IntRange rowRange, boolean replace);
    default R row(Material type, int amount, IntRange rowRange, boolean replace) { return row(new ItemStack(type, amount), rowRange, replace); }

    R rows(ItemStack item, boolean replace, int... rows);
    default R rows(Material type, int amount, boolean replace, int... rows) { return rows(new ItemStack(type, amount), replace, rows); }

    R rows(ItemStack item, boolean replace, Iterable<Integer> rows);
    default R rows(Material type, int amount, boolean replace, Iterable<Integer> rows) { return rows(new ItemStack(type, amount), replace, rows); }
    
    // --- column fillers
    R column(ItemStack item, int column, boolean replace);
    default R column(Material type, int amount, int column, boolean replace) { return column(new ItemStack(type, amount), column, replace); }

    R column(ItemStack item, int sourceColumn, int destColumn, boolean replace);
    default R column(Material type, int amount, int sourceColumn, int destColumn, boolean replace) { return column(new ItemStack(type, amount), sourceColumn, destColumn, replace); }

    R column(ItemStack item, IntRange columnRange, boolean replace);
    default R column(Material type, int amount, IntRange columnRange, boolean replace) { return column(new ItemStack(type, amount), columnRange, replace); }

    R columns(ItemStack item, boolean replace, int... columns);
    default R columns(Material type, int amount, boolean replace, int... columns) { return columns(new ItemStack(type, amount), replace, columns); }

    R columns(ItemStack item, boolean replace, Iterable<Integer> columns);
    default R columns(Material type, int amount, boolean replace, Iterable<Integer> columns) { return columns(new ItemStack(type, amount), replace, columns); }
    
    R remove(int slot);
    R remove(int x, int y);
    
    R removeAll();
    R removeAll(IntRange slotRange);
    R removeAll(int sourceSlot, int destSlot);
    R removeAll(IntRange xRange, IntRange yRange);
    R removeAll(int fromX, int fromY, int toX, int toY);
    
    C pushSync();
    CompletableFuture<C> pushAsync();
    CompletableFuture<C> pushLater(long duration, TimeUnit timeUnit);
    
}
