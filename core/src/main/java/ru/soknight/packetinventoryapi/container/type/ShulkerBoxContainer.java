package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerStub;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.RowableContainerStub;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequestStub;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.util.IntRange;

public class ShulkerBoxContainer extends ContainerStub<ShulkerBoxContainer> implements RowableContainerStub<ShulkerBoxContainer> {

    private ShulkerBoxContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.SHULKER_BOX, title);
    }

    private ShulkerBoxContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.SHULKER_BOX, title);
    }

    public static ShulkerBoxContainer create(Player inventoryHolder, String title) {
        return new ShulkerBoxContainer(inventoryHolder, title);
    }

    public static ShulkerBoxContainer create(Player inventoryHolder, BaseComponent title) {
        return new ShulkerBoxContainer(inventoryHolder, title);
    }

    public static ShulkerBoxContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.SHULKER_BOX);
    }

    @Override
    protected ShulkerBoxContainer getThis() {
        return this;
    }

    @Override
    public ShulkerBoxContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public int getRowsAmount() {
        return 3;
    }

    @Override
    public ShulkerBoxContainer setRowsAmount(int rowsAmount) {
        return this; // nothing here, rows amount is constant for the shulker box inventory :)
    }

    @Override
    public ContentUpdateRequestStub<ShulkerBoxContainer> updateContent() {
        return ContentUpdateRequestStub.create(this, contentData, 0);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public ShulkerBoxContainer clickListener(int x, int y, WindowClickListener<ShulkerBoxContainer, ContentUpdateRequestStub<ShulkerBoxContainer>> listener) {
        Validate.isTrue(x < 0 || x > 8, "'x' must be in the range [0-8]");
        Validate.isTrue(y < 0 || y > 2, "'y' must be in the range [0-2]");
        
        super.clickListener(x * 9 + y, listener);
        return this;
    }
    
    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, 26);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(27, 53);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(54, 62);
    }
    
}
