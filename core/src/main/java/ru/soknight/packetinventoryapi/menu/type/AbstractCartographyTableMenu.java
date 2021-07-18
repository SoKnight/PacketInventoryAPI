package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.CartographyTableContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractCartographyTableMenu extends AbstractMenu<CartographyTableContainer, CartographyTableContainer.CartographyTableUpdateRequest> {

    public AbstractCartographyTableMenu(String name, Plugin providingPlugin) {
        this(CartographyTableContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractCartographyTableMenu(String name, Plugin providingPlugin, String title) {
        this(CartographyTableContainer.create(null, title), name, providingPlugin);
    }

    public AbstractCartographyTableMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(CartographyTableContainer.create(null, title), name, providingPlugin);
    }

    public AbstractCartographyTableMenu(CartographyTableContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
