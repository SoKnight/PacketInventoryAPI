package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.DropperContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractDropperMenu extends AbstractMenu<DropperContainer, DropperContainer.DropperUpdateRequest> {

    public AbstractDropperMenu(String name, Plugin providingPlugin) {
        this(DropperContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractDropperMenu(String name, Plugin providingPlugin, String title) {
        this(DropperContainer.create(null, title), name, providingPlugin);
    }

    public AbstractDropperMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(DropperContainer.create(null, title), name, providingPlugin);
    }

    public AbstractDropperMenu(DropperContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
