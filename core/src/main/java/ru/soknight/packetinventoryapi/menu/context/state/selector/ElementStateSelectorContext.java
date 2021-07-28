package ru.soknight.packetinventoryapi.menu.context.state.selector;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;

public interface ElementStateSelectorContext extends StateSelectorContext {

    static ElementStateSelectorContext create(Player viewer, StateableMenuItem menuItem, int slot, int pageIndex, int totalIndex) {
        return new SimpleElementStateSelectorContext(viewer, menuItem, slot, pageIndex, totalIndex);
    }

    int getSlot();

    int getPageIndex();

    int getTotalIndex();

}
