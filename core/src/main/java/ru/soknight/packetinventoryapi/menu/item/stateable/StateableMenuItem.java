package ru.soknight.packetinventoryapi.menu.item.stateable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @Nullable StateSelector getStateSelector();

    StateableMenuItem setStateSelector(StateSelector stateSelector);

    @Override
    default @NotNull RegularMenuItem<?, ?> getItemFor(Player viewer) {
        return selectStateItem(viewer);
    }

    @Nullable RegularMenuItem<?, ?> selectStateItem(Player player);

    interface Builder {
        StateableMenuItem build();

        Builder addStateItem(String id, RegularMenuItem<?, ?> regularItem);

        Builder addStateItems(Map<String, RegularMenuItem<?, ?>> stateItems);
    }

}
