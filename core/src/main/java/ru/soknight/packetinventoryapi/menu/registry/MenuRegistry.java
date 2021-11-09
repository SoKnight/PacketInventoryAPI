package ru.soknight.packetinventoryapi.menu.registry;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.exception.menu.InvalidMethodStructureException;
import ru.soknight.packetinventoryapi.exception.menu.MenuRegisteredAlreadyException;
import ru.soknight.packetinventoryapi.exception.menu.RegistrationDeniedException;
import ru.soknight.packetinventoryapi.menu.Menu;

import java.util.Map;

public interface MenuRegistry {

    static @NotNull MenuRegistry create() {
        return new SimpleMenuRegistry();
    }

    @NotNull Map<String, Menu<?, ?>> getRegisteredMenus();

    @Nullable Menu<?, ?> getViewingMenu(Player player);

    void fireEvent(@NotNull Menu<?, ?> menu, @NotNull Event<?, ?> event);

    void register(@NotNull Menu<?, ?> menu) throws InvalidMethodStructureException, RegistrationDeniedException, MenuRegisteredAlreadyException;

    boolean isRegistered(@NotNull Menu<?, ?> menu);

    boolean isRegistered(@NotNull Plugin plugin, @NotNull String menuId);

    boolean isRegistered(@NotNull String pluginName, @NotNull String menuId);

    boolean unregister(@NotNull Menu<?, ?> menu);

    boolean unregister(@NotNull Plugin plugin, String menuId);

    boolean unregister(@NotNull String pluginName, String menuId);

}
