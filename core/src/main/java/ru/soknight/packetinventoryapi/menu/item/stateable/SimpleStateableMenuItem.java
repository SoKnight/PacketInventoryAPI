package ru.soknight.packetinventoryapi.menu.item.stateable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.menu.context.state.selector.ElementStateSelectorContext;
import ru.soknight.packetinventoryapi.menu.context.state.selector.StateSelectorContext;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

final class SimpleStateableMenuItem implements StateableMenuItem {

    private final ConfigurationSection configuration;
    private final Map<String, RegularMenuItem<?, ?>> stateItems;
    private StateSelector<?> stateSelector;

    public SimpleStateableMenuItem(ConfigurationSection configuration) {
        this.configuration = configuration;
        this.stateItems = new LinkedHashMap<>();
    }

    @Override
    public @NotNull ItemStack asBukkitItemFor(Player viewer) {
        RegularMenuItem<?, ?> stateItem = selectStateItem(viewer);
        return stateItem != null ? stateItem.asBukkitItemFor(viewer) : StateSelector.EMPTY_ITEM;
    }

    @Override
    public @NotNull Map<String, RegularMenuItem<?, ?>> getStateItems() {
        return Collections.unmodifiableMap(stateItems);
    }

    @Override
    public @Nullable ConfigurationSection getConfiguration() {
        return configuration;
    }

    @Override
    public @NotNull Set<String> getStates() {
        return Collections.unmodifiableSet(stateItems.keySet());
    }

    @Override
    public @Nullable RegularMenuItem<?, ?> getStateItem(String id) {
        return stateItems.get(id);
    }

    @Override
    public boolean hasStateItem(String id) {
        return stateItems.containsKey(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <CTX extends StateSelectorContext> @Nullable StateSelector<CTX> getStateSelector() {
        return (StateSelector<CTX>) stateSelector;
    }

    @Override
    public <CTX extends StateSelectorContext> SimpleStateableMenuItem setStateSelector(StateSelector<CTX> stateSelector) {
        this.stateSelector = stateSelector;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <CTX extends StateSelectorContext> @Nullable RegularMenuItem<?, ?> selectStateItem(Player player) {
        try {
            StateSelector<CTX> stateSelector = getStateSelector();
            return stateSelector != null
                    ? stateSelector.selectState((CTX) StateSelectorContext.create(player, this))
                    : null;
        } catch (ClassCastException ignored) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <CTX extends StateSelectorContext> @Nullable RegularMenuItem<?, ?> selectStateItem(Player player, int slot, int pageIndex, int totalIndex) {
        try {
            StateSelector<CTX> stateSelector = getStateSelector();
            return stateSelector != null
                    ? stateSelector.selectState((CTX) ElementStateSelectorContext.create(player, this, slot, pageIndex, totalIndex))
                    : null;
        } catch (ClassCastException ignored) {
            return null;
        }
    }

    static Builder build(ConfigurationSection configuration) {
        return new Builder(new SimpleStateableMenuItem(configuration));
    }

    static final class Builder implements StateableMenuItem.Builder {
        private final SimpleStateableMenuItem stateableItem;

        private Builder(SimpleStateableMenuItem stateableItem) {
            this.stateableItem = stateableItem;
        }

        @Override
        public SimpleStateableMenuItem build() {
            return stateableItem;
        }

        @Override
        public Builder addStateItem(String id, RegularMenuItem<?, ?> regularItem) {
            stateableItem.stateItems.put(id, regularItem);
            return this;
        }

        @Override
        public Builder addStateItems(Map<String, RegularMenuItem<?, ?>> stateItems) {
            stateableItem.stateItems.putAll(stateItems);
            return this;
        }
    }

}
