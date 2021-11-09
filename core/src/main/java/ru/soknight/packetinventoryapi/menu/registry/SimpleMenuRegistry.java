package ru.soknight.packetinventoryapi.menu.registry;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.exception.menu.InvalidMethodStructureException;
import ru.soknight.packetinventoryapi.exception.menu.MenuRegisteredAlreadyException;
import ru.soknight.packetinventoryapi.exception.menu.RegistrationDeniedException;
import ru.soknight.packetinventoryapi.menu.Menu;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class SimpleMenuRegistry implements MenuRegistry {

    private final Map<IdentifyTicket, RegistrationBundle<?, ?>> registrations;
    private boolean available;

    public SimpleMenuRegistry() {
        this.registrations = new ConcurrentHashMap<>();
        this.available = true;
    }

    @Override
    public @NotNull Map<String, Menu<?, ?>> getRegisteredMenus() {
        Map<String, Menu<?, ?>> menus = new HashMap<>();
        if(registrations.isEmpty())
            return menus;

        registrations.forEach((ticket, registration) -> menus.put(ticket.getMenuId(), registration.getMenu()));
        return menus;
    }

    @Override
    public Menu<?, ?> getViewingMenu(Player player) {
        return findRegistration(player)
                .map(RegistrationBundle::getMenu)
                .orElse(null);
    }

    @Override
    public void fireEvent(Menu<?, ?> menu, Event<?, ?> event) {
        findRegistration(menu).ifPresent(registration -> registration.fireEvent(event));
    }

    private Optional<RegistrationBundle<?, ?>> findRegistration(Player viewer) {
        return registrations.values()
                .stream()
                .filter(bundle -> bundle.isViewing(viewer))
                .findAny();
    }

    private Optional<RegistrationBundle<?, ?>> findRegistration(Menu<?, ?> menu) {
        return registrations.values()
                .stream()
                .filter(bundle -> bundle.isRepresents(menu))
                .findAny();
    }

    @Override
    public void register(Menu<?, ?> menu) throws
            InvalidMethodStructureException,
            RegistrationDeniedException,
            MenuRegisteredAlreadyException
    {
        if(!available)
            throw new RegistrationDeniedException();

        Validate.notNull(menu, "menu");
        if(isRegistered(menu))
            throw new MenuRegisteredAlreadyException(menu);

        RegistrationBundle<?, ?> registration = new RegistrationBundle<>(menu);
        registration.registerListeners();

        registrations.put(registration.getTicket(), registration);
    }

    @Override
    public boolean isRegistered(Menu<?, ?> menu) {
        Validate.notNull(menu, "menu");
        return isRegistered(menu.getProvidingPlugin(), menu.getName());
    }

    @Override
    public boolean isRegistered(Plugin plugin, String name) {
        Validate.notNull(plugin, "plugin");
        Validate.notEmpty(name, "name");
        return isRegistered(plugin.getName(), name);
    }

    @Override
    public boolean isRegistered(String plugin, String name) {
        Validate.notEmpty(plugin, "plugin");
        Validate.notEmpty(name, "name");
        return registrations.containsKey(IdentifyTicket.create(plugin, name));
    }

    @Override
    public boolean unregister(Menu<?, ?> menu) {
        Validate.notNull(menu, "menu");
        return unregister(menu.getProvidingPlugin(), menu.getName());
    }

    @Override
    public boolean unregister(Plugin plugin, String name) {
        Validate.notNull(plugin, "plugin");
        Validate.notEmpty(name, "name");
        return unregister(plugin.getName(), name);
    }

    @Override
    public boolean unregister(String plugin, String name) {
        Validate.notEmpty(plugin, "plugin");
        Validate.notEmpty(name, "name");
        return registrations.remove(IdentifyTicket.create(plugin, name)) != null;
    }

    public void unregisterAll() {
        available = false;

        registrations.keySet().removeIf(this::unregisterSafety);
    }

    private boolean unregisterSafety(IdentifyTicket ticket) {
        RegistrationBundle<?, ?> registration = registrations.get(ticket);
        if(registration != null) {
            Menu<?, ?> menu = registration.getMenu();
            if(menu != null) {
                menu.getContainer().closeAll();
            }
        }

        return true;
    }

}
