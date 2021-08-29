package ru.soknight.packetinventoryapi.item.update.content;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataBundle;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.exception.item.filler.InvalidFillerException;
import ru.soknight.packetinventoryapi.exception.item.filler.InvalidRowFillerException;
import ru.soknight.packetinventoryapi.item.update.content.filler.Filler;
import ru.soknight.packetinventoryapi.item.update.content.filler.column.ColumnFiller;
import ru.soknight.packetinventoryapi.item.update.content.filler.row.RowFiller;
import ru.soknight.packetinventoryapi.menu.context.Context;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

public interface ContentUpdateRequest<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> R create(C container, Map<Integer, ItemStack> contentData) {
        return new BaseContentUpdateRequest<>(container, contentData).getThis();
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> R create(C container, Map<Integer, ItemStack> contentData, int slotsOffset) {
        return new BaseContentUpdateRequest<>(container, contentData, slotsOffset).getThis();
    }

    @NotNull C getContainer();

    boolean isPushed();

    boolean isViewingPlayerInventory();
    R setViewingPlayerInventory(boolean value);

    default R doViewPlayerInventory() { return setViewingPlayerInventory(true); }
    default R doHidePlayerInventory() { return setViewingPlayerInventory(false); }

    boolean isViewingHotbarContent();
    R setViewingHotbarContent(boolean value);

    default R doViewHotbarContent() { return setViewingHotbarContent(true); }
    default R doHideHotbarContent() { return setViewingHotbarContent(false); }

    @NotNull Map<Integer, ItemStack> getItemMatrix();
    R resetItemMatrix();

    boolean isSet(int slot);

    // --- using parsed data bundle
    R fromParsedData(@NotNull Iterable<? extends MenuItem> items, boolean replace);
    default R fromParsedData(@NotNull ParsedDataBundle parsedDataBundle, boolean replace) {
        return fromParsedData(parsedDataBundle.getContent().values(), replace);
    }

    // --- menu item insertion
    R insert(@NotNull DisplayableMenuItem item, boolean replace);
    R remove(@NotNull DisplayableMenuItem item);

    // --- setters
    R set(@NotNull ItemStack item, int slot, boolean replace);
    R set(@NotNull IntFunction<ItemStack> itemProvider, int slot, boolean replace);
    default R set(@NotNull Material type, int slot, boolean replace) { return set(type, 1, slot, replace); }
    default R set(@NotNull Material type, int amount, int slot, boolean replace) { return set(new ItemStack(type, amount), slot, replace); }

    R setAt(@NotNull ItemStack item, int x, int y, boolean replace);
    R setAt(@NotNull IntFunction<ItemStack> itemProvider, int x, int y, boolean replace);
    default R setAt(@NotNull Material type, int x, int y, boolean replace) { return setAt(type, 1, x, y, replace); }
    default R setAt(@NotNull Material type, int amount, int x, int y, boolean replace) { return setAt(new ItemStack(type, amount), x, y, replace); }
    
    // --- filler
    R fill(@NotNull Filler filler) throws InvalidFillerException;

    // --- row filler
    R fillRow(@NotNull RowFiller rowFiller) throws InvalidRowFillerException;

    // --- column filler
    R fillColumn(@NotNull ColumnFiller columnFiller);

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
