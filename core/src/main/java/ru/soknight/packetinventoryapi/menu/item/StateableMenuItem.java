package ru.soknight.packetinventoryapi.menu.item;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface StateableMenuItem extends MenuItem {

    static StateableMenuItem.Builder createNew() {
        return SimpleStateableMenuItem.build();
    }

    @NotNull Map<String, RegularMenuItem<?, ?>> getStateItems();

    @NotNull Set<String> getStates();

    @Nullable RegularMenuItem<?, ?> getStateItem(String id);

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
