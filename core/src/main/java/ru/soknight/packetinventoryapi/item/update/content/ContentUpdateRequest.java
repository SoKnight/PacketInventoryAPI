package ru.soknight.packetinventoryapi.item.update.content;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataBundle;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.exception.item.ItemInsertFailedException;
import ru.soknight.packetinventoryapi.exception.item.filler.InvalidFillerException;
import ru.soknight.packetinventoryapi.exception.item.filler.InvalidRowFillerException;
import ru.soknight.packetinventoryapi.item.update.content.filler.Filler;
import ru.soknight.packetinventoryapi.item.update.content.filler.column.ColumnFiller;
import ru.soknight.packetinventoryapi.item.update.content.filler.row.RowFiller;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

public interface ContentUpdateRequest<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> @NotNull R create(
            @NotNull C container,
            @NotNull Map<Integer, ItemStack> contentData
    ) {
        return new BaseContentUpdateRequest<>(container, contentData).getThis();
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> @NotNull R create(
            @NotNull C container,
            @NotNull Map<Integer, ItemStack> contentData,
            int slotsOffset
    ) {
        return new BaseContentUpdateRequest<>(container, contentData, slotsOffset).getThis();
    }

    @NotNull C getContainer();

    boolean isPushed();

    boolean isViewingPlayerInventory();
    @NotNull R setViewingPlayerInventory(boolean value);

    default @NotNull R doViewPlayerInventory() { return setViewingPlayerInventory(true); }
    default @NotNull R doHidePlayerInventory() { return setViewingPlayerInventory(false); }

    boolean isViewingHotbarContent();
    @NotNull R setViewingHotbarContent(boolean value);

    default @NotNull R doViewHotbarContent() { return setViewingHotbarContent(true); }
    default @NotNull R doHideHotbarContent() { return setViewingHotbarContent(false); }

    @NotNull Map<Integer, ItemStack> getItemMatrix();
    @NotNull R resetItemMatrix();

    boolean isSet(int slot);

    // --- using parsed data bundle
    @NotNull R fromParsedData(@NotNull Iterable<? extends MenuItem> items, boolean replace);
    default @NotNull R fromParsedData(@NotNull ParsedDataBundle parsedDataBundle, boolean replace) {
        return fromParsedData(parsedDataBundle.getContent().values(), replace);
    }

    // --- menu item insertion
    @NotNull R insert(@NotNull DisplayableMenuItem item, boolean replace) throws ItemInsertFailedException;
    @NotNull R remove(@NotNull DisplayableMenuItem item);

    // --- setters
    @NotNull R set(@Nullable ItemStack item, int slot, boolean replace);
    @NotNull R set(@NotNull IntFunction<ItemStack> itemProvider, int slot, boolean replace);
    default @NotNull R set(@NotNull Material type, int slot, boolean replace) { return set(type, 1, slot, replace); }
    default @NotNull R set(@NotNull Material type, int amount, int slot, boolean replace) { return set(new ItemStack(type, amount), slot, replace); }

    @NotNull R setAt(@Nullable ItemStack item, int x, int y, boolean replace);
    @NotNull R setAt(@NotNull IntFunction<ItemStack> itemProvider, int x, int y, boolean replace);
    default @NotNull R setAt(@NotNull Material type, int x, int y, boolean replace) { return setAt(type, 1, x, y, replace); }
    default @NotNull R setAt(@NotNull Material type, int amount, int x, int y, boolean replace) { return setAt(new ItemStack(type, amount), x, y, replace); }
    
    // --- filler
    @NotNull R fill(@NotNull Filler filler) throws InvalidFillerException;

    // --- row filler
    @NotNull R fillRow(@NotNull RowFiller rowFiller) throws InvalidRowFillerException;

    // --- column filler
    @NotNull R fillColumn(@NotNull ColumnFiller columnFiller);

    @NotNull R remove(int slot);
    @NotNull R remove(int x, int y);

    @NotNull R removeAll();
    @NotNull R removeAll(@NotNull IntRange slotRange);
    @NotNull R removeAll(int sourceSlot, int destSlot);
    @NotNull R removeAll(@NotNull IntRange xRange, @NotNull IntRange yRange);
    @NotNull R removeAll(int fromX, int fromY, int toX, int toY);

    @NotNull C pushSync();
    @NotNull CompletableFuture<C> pushAsync();
    @NotNull CompletableFuture<C> pushLater(long duration, @NotNull TimeUnit timeUnit);
    
}
