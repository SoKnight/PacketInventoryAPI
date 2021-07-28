package ru.soknight.packetinventoryapi.menu.context.state.selector;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;

final class SimpleStateSelectorContext extends AbstractStateSelectorContext {

    SimpleStateSelectorContext(Player viewer, StateableMenuItem menuItem) {
        super(viewer, menuItem);
    }

}
