package ru.soknight.packetinventoryapi.menu.item.stateable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.menu.context.state.selector.StateSelectorContext;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;

import java.util.Map;
import java.util.Set;

public interface StateableMenuItem extends DisplayableMenuItem {

    static @NotNull StateableMenuItem.Builder create(@NotNull ConfigurationSection configuration) {
        return SimpleStateableMenuItem.build(configuration);
    }

    @NotNull Map<String, RegularMenuItem<?, ?>> getStateItems();

    @NotNull Set<String> getStates();

    @Nullable RegularMenuItem<?, ?> getStateItem(String id);

    boolean hasStateItem(String id);

    @Nullable <CTX extends StateSelectorContext> StateSelector<CTX> getStateSelector();

    <CTX extends StateSelectorContext> StateableMenuItem setStateSelector(StateSelector<CTX> stateSelector);

    @Override
    default @NotNull RegularMenuItem<?, ?> getItemFor(Player viewer) {
        return selectStateItem(viewer);
    }

    default @NotNull RegularMenuItem<?, ?> getItemFor(Player viewer, int slot, int pageIndex, int totalIndex) {
        return selectStateItem(viewer, slot, pageIndex, totalIndex);
    }

    <CTX extends StateSelectorContext> @Nullable RegularMenuItem<?, ?> selectStateItem(Player player);

    <CTX extends StateSelectorContext> @Nullable RegularMenuItem<?, ?> selectStateItem(Player player, int slot, int pageIndex, int totalIndex);

    interface Builder {
        StateableMenuItem build();

        Builder addStateItem(String id, RegularMenuItem<?, ?> regularItem);

        Builder addStateItems(Map<String, RegularMenuItem<?, ?>> stateItems);
    }

}
