package ru.soknight.packetinventoryapi.menu;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemStructure;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataBundle;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.menu.container.PublicWrapper;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;

import java.util.Map;

public interface Menu<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    @NotNull PublicWrapper<C, R> getContainer();

    @NotNull C getOriginalContainer();

    @NotNull DataHolder getDataHolder(Player player);

    @NotNull ConfigurationItemStructure<Menu<C, R>> configurationStructure();

    @NotNull String getName();

    @NotNull Plugin getProvidingPlugin();

    boolean open(@NotNull Player player);

    boolean isViewing(@NotNull Player player);

    boolean close(@NotNull Player player);

    void closeAll();

    @NotNull Map<String, MenuItem> getMenuItems();

    @Nullable MenuItem getMenuItem(@NotNull String id);

    void updateParsedData(@NotNull ParsedDataBundle parsedData);

}
