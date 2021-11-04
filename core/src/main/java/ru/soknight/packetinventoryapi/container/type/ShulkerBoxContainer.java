package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;
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

public class ShulkerBoxContainer extends ContainerStub<ShulkerBoxContainer> implements RowableContainerStub<ShulkerBoxContainer> {

    private ShulkerBoxContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.SHULKER_BOX, title);
    }

    private ShulkerBoxContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.SHULKER_BOX, title);
    }

    public static @NotNull ShulkerBoxContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new ShulkerBoxContainer(inventoryHolder, title);
    }

    public static @NotNull ShulkerBoxContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new ShulkerBoxContainer(inventoryHolder, title);
    }

    public static @NotNull ShulkerBoxContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.SHULKER_BOX);
    }

    @Override
    protected @NotNull ShulkerBoxContainer getThis() {
        return this;
    }

    @Override
    public @NotNull ShulkerBoxContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public int getRowsAmount() {
        return 3;
    }

    @Override
    public @NotNull ShulkerBoxContainer setRowsAmount(int rowsAmount) {
        return this; // nothing here, rows amount is constant for the shulker box inventory :)
    }

    @Override
    public @NotNull ContentUpdateRequestStub<ShulkerBoxContainer> updateContent() {
        return ContentUpdateRequestStub.create(this, contentData, 0);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public @NotNull ShulkerBoxContainer clickListener(int x, int y, @NotNull WindowClickListener<ShulkerBoxContainer, ContentUpdateRequestStub<ShulkerBoxContainer>> listener) {
        Validate.isTrue(x < 0 || x > 8, "'x' must be in the range [0-8]");
        Validate.isTrue(y < 0 || y > 2, "'y' must be in the range [0-2]");
        return clickListener(x * 9 + y, listener);
    }
    
    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0, 26);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(27, 53);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(54, 62);
    }
    
}
