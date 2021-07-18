package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.GenericContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenuStub;

public abstract class AbstractGenericMenu extends AbstractMenuStub<GenericContainer> {

    public AbstractGenericMenu(String name, Plugin providingPlugin, int rows) {
        this(name, providingPlugin, rows, "");
    }

    public AbstractGenericMenu(String name, Plugin providingPlugin, int rows, String title) {
        this(GenericContainer.create(null, rows, title), name, providingPlugin);
    }

    public AbstractGenericMenu(String name, Plugin providingPlugin, int rows, BaseComponent title) {
        this(GenericContainer.create(null, rows, title), name, providingPlugin);
    }

    public AbstractGenericMenu(GenericContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
