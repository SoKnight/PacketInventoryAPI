package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.ContainerStub;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.RowableContainerStub;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequestStub;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.util.IntRange;

@Getter
public class GenericContainer extends ContainerStub<GenericContainer> implements RowableContainerStub<GenericContainer> {

    private int rowsAmount;
    
    private GenericContainer(Player inventoryHolder, int rows, String title) {
        super(inventoryHolder, ContainerType.valueOf("GENERIC_9X" + rows), title);
        this.rowsAmount = rows;
    }

    private GenericContainer(Player inventoryHolder, int rows, BaseComponent title) {
        super(inventoryHolder, ContainerType.valueOf("GENERIC_9X" + rows), title);
        this.rowsAmount = rows;
    }
    
    public static GenericContainer create(Player inventoryHolder, int rows, String title) {
        Validate.isTrue(rows >= 1 && rows <= 6, "'rows' must be in the range [1;6]");
        return new GenericContainer(inventoryHolder, rows, title);
    }

    public static GenericContainer create(Player inventoryHolder, int rows, BaseComponent title) {
        Validate.isTrue(rows >= 1 && rows <= 6, "'rows' must be in the range [1;6]");
        return new GenericContainer(inventoryHolder, rows, title);
    }

    @Override
    protected GenericContainer getThis() {
        return this;
    }

    @Override
    public GenericContainer copy(Player holder) {
        return create(holder, rowsAmount, title.duplicate());
    }

    @Override
    public GenericContainer setRowsAmount(int rowsAmount) {
        Validate.isTrue(rowsAmount > 0 && rowsAmount <= 6, "'rowsAmount' must be in the range [1;6]");

        this.rowsAmount = rowsAmount;
        return this;
    }

    @Override
    public ContentUpdateRequestStub<GenericContainer> updateContent() {
        return ContentUpdateRequestStub.create(getThis(), contentData, 0);
    }

    /**********************
     *  Events listening  *
     *********************/
    
    public GenericContainer clickListener(int x, int y, WindowClickListener<GenericContainer, ContentUpdateRequestStub<GenericContainer>> listener) {
        Validate.isTrue(x >= 0 && x <= 8, "'x' must be in the range [0-8]");
        Validate.isTrue(y >= 0 && y <= rowsAmount - 1, "'y' must be in the range [0-" + (rowsAmount - 1) + "]");
        
        return super.clickListener(x * 9 + y, listener);
    }

    public GenericContainer containerClickListener(WindowClickListener<GenericContainer, ContentUpdateRequestStub<GenericContainer>> listener) {
        return super.clickListener(containerSlots(), listener);
    }
    
    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0, rowsAmount * 9 - 1);
    }

    @Override
    public IntRange playerInventorySlots() {
        int from = rowsAmount * 9;
        return new IntRange(from, from + 26);
    }

    @Override
    public IntRange playerHotbarSlots() {
        int from = rowsAmount * 9 + 27;
        return new IntRange(from, from + 8);
    }

}
