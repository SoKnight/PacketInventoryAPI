package ru.soknight.packetinventoryapi.menu.registry;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.exception.menu.InvalidMethodStructureException;
import ru.soknight.packetinventoryapi.exception.menu.MenuRegisteredAlreadyException;
import ru.soknight.packetinventoryapi.exception.menu.RegistrationDeniedException;
import ru.soknight.packetinventoryapi.menu.Menu;

public interface MenuRegistry {

    static MenuRegistry create() {
        return new SimpleMenuRegistry();
    }

    Menu<?, ?> getViewingMenu(Player player);

    void fireEvent(Menu<?, ?> menu, Event<?, ?> event);

    void register(Menu<?, ?> menu) throws InvalidMethodStructureException, RegistrationDeniedException, MenuRegisteredAlreadyException;

    boolean isRegistered(Menu<?, ?> menu);

    boolean isRegistered(Plugin plugin, String name);

    boolean isRegistered(String plugin, String name);

    boolean unregister(Menu<?, ?> menu);

    boolean unregister(Plugin plugin, String name);

    boolean unregister(String plugin, String name);

}
