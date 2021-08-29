package ru.soknight.packetinventoryapi.menu.context.state.selector;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;

@Getter
final class SimpleElementStateSelectorContext extends AbstractStateSelectorContext implements ElementStateSelectorContext {

    private final int pageIndex;
    private final int totalIndex;

    SimpleElementStateSelectorContext(Player viewer, StateableMenuItem menuItem, int slot, int pageIndex, int totalIndex) {
        super(viewer, menuItem, slot);
        this.pageIndex = pageIndex;
        this.totalIndex = totalIndex;
    }

}
