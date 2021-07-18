package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.BrewingStandContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractBrewingStandMenu extends AbstractMenu<BrewingStandContainer, BrewingStandContainer.BrewingStandUpdateRequest> {

    public AbstractBrewingStandMenu(String name, Plugin providingPlugin) {
        this(BrewingStandContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractBrewingStandMenu(String name, Plugin providingPlugin, String title) {
        this(BrewingStandContainer.create(null, title), name, providingPlugin);
    }

    public AbstractBrewingStandMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(BrewingStandContainer.create(null, title), name, providingPlugin);
    }

    public AbstractBrewingStandMenu(BrewingStandContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
