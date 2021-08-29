package ru.soknight.packetinventoryapi.menu.context.state.selector;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;

@Getter
public abstract class AbstractStateSelectorContext implements StateSelectorContext {

    protected final Player viewer;
    protected final StateableMenuItem menuItem;
    protected final int slot;

    protected AbstractStateSelectorContext(Player viewer, StateableMenuItem menuItem, int slot) {
        this.viewer = viewer;
        this.menuItem = menuItem;
        this.slot = slot;
    }

}
