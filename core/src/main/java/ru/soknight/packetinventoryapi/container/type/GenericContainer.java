package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerStub;
import ru.soknight.packetinventoryapi.container.ContainerTypes;
import ru.soknight.packetinventoryapi.container.RowableContainerStub;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequestStub;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

@Getter
public class GenericContainer extends ContainerStub<GenericContainer> implements RowableContainerStub<GenericContainer> {

    private int rowsAmount;
    
    private GenericContainer(@Nullable Player inventoryHolder, int rows, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.valueOf("GENERIC_9X" + rows), title);
        this.rowsAmount = rows;
    }

    private GenericContainer(@Nullable Player inventoryHolder, int rows, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.valueOf("GENERIC_9X" + rows), title);
        this.rowsAmount = rows;
    }
    
    public static @NotNull GenericContainer create(@Nullable Player inventoryHolder, int rows, @Nullable String title) {
        Validate.isTrue(rows >= 1 && rows <= 6, "'rows' must be in the range [1;6]");
        return new GenericContainer(inventoryHolder, rows, title);
    }

    public static @NotNull GenericContainer create(@Nullable Player inventoryHolder, int rows, @NotNull BaseComponent title) {
        Validate.isTrue(rows >= 1 && rows <= 6, "'rows' must be in the range [1;6]");
        return new GenericContainer(inventoryHolder, rows, title);
    }

    public static @NotNull GenericContainer createDefault(@Nullable Player inventoryHolder, int rows) {
        return create(inventoryHolder, rows, ContainerLocaleTitles.CHEST);
    }

    @Override
    protected @NotNull GenericContainer getThis() {
        return this;
    }

    @Override
    public @NotNull GenericContainer copy(@Nullable Player holder) {
        return create(holder, rowsAmount, title.duplicate());
    }

    @Override
    public @NotNull GenericContainer setRowsAmount(int rowsAmount) {
        Validate.isTrue(rowsAmount > 0 && rowsAmount <= 6, "'rowsAmount' must be in the range [1;6]");
        this.rowsAmount = rowsAmount;
        return this;
    }

    @Override
    public @NotNull ContentUpdateRequestStub<GenericContainer> updateContent() {
        return ContentUpdateRequestStub.create(getThis(), contentData, 0);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public @NotNull GenericContainer clickListener(int x, int y, @NotNull WindowClickListener<GenericContainer, ContentUpdateRequestStub<GenericContainer>> listener) {
        Validate.isTrue(x >= 0 && x <= 8, "'x' must be in the range [0-8]");
        Validate.isTrue(y >= 0 && y <= rowsAmount - 1, "'y' must be in the range [0-" + (rowsAmount - 1) + "]");
        return clickListener(x * 9 + y, listener);
    }

    public @NotNull GenericContainer containerClickListener(@NotNull WindowClickListener<GenericContainer, ContentUpdateRequestStub<GenericContainer>> listener) {
        return clickListener(containerSlots(), listener);
    }
    
    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, rowsAmount * 9 - 1);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        int from = rowsAmount * 9;
        return new IntRange(from, from + 26);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        int from = rowsAmount * 9 + 27;
        return new IntRange(from, from + 8);
    }

}
