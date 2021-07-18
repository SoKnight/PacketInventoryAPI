package ru.soknight.packetinventoryapi.event.window;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;

@Getter
public class WindowClickEvent<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> extends Event<C, R> {

    private final int clickedSlot;
    private final WindowClickType clickType;
    private final ItemStack clickedItem;

    public WindowClickEvent(
            Player actor,
            int clickedSlot,
            WindowClickType clickType,
            ItemStack clickedItem
    ) {
        this(actor, null, clickedSlot, clickType, clickedItem);
    }

    public WindowClickEvent(
            Player actor,
            C container,
            int clickedSlot,
            WindowClickType clickType,
            ItemStack clickedItem
    ) {
        super(actor, container);
        this.clickedSlot = clickedSlot;
        this.clickType = clickType;
        this.clickedItem = clickedItem;
    }

    @Override
    public String toString() {
        return "WindowClickEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", clickedSlot=" + clickedSlot +
                ", clickType=" + clickType +
                ", clickedItem=" + clickedItem +
                '}';
    }

}
