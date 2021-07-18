package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.SmithingTableContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractSmithingTableMenu extends AbstractMenu<SmithingTableContainer, SmithingTableContainer.SmithingTableUpdateRequest> {

    public AbstractSmithingTableMenu(String name, Plugin providingPlugin) {
        this(SmithingTableContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractSmithingTableMenu(String name, Plugin providingPlugin, String title) {
        this(SmithingTableContainer.create(null, title), name, providingPlugin);
    }

    public AbstractSmithingTableMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(SmithingTableContainer.create(null, title), name, providingPlugin);
    }

    public AbstractSmithingTableMenu(SmithingTableContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
