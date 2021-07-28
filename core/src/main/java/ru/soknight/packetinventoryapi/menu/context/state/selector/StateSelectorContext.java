package ru.soknight.packetinventoryapi.menu.context.state.selector;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.menu.context.Context;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;

public interface StateSelectorContext extends Context {

    static StateSelectorContext create(Player viewer, StateableMenuItem menuItem) {
        return new SimpleStateSelectorContext(viewer, menuItem);
    }

    @NotNull StateableMenuItem getMenuItem();

}
