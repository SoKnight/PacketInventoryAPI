package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.GrindstoneContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractGrindstoneMenu extends AbstractMenu<GrindstoneContainer, GrindstoneContainer.GrindstoneUpdateRequest> {

    public AbstractGrindstoneMenu(String name, Plugin providingPlugin) {
        this(GrindstoneContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractGrindstoneMenu(String name, Plugin providingPlugin, String title) {
        this(GrindstoneContainer.create(null, title), name, providingPlugin);
    }

    public AbstractGrindstoneMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(GrindstoneContainer.create(null, title), name, providingPlugin);
    }

    public AbstractGrindstoneMenu(GrindstoneContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
