package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.HopperContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractHopperMenu extends AbstractMenu<HopperContainer, HopperContainer.HopperUpdateRequest> {

    public AbstractHopperMenu(String name, Plugin providingPlugin) {
        this(HopperContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractHopperMenu(String name, Plugin providingPlugin, String title) {
        this(HopperContainer.create(null, title), name, providingPlugin);
    }

    public AbstractHopperMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(HopperContainer.create(null, title), name, providingPlugin);
    }

    public AbstractHopperMenu(HopperContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
