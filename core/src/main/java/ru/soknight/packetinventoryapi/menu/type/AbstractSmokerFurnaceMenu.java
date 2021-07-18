package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.SmokerFurnaceContainer;

public abstract class AbstractSmokerFurnaceMenu extends AbstractFurnaceMenu<SmokerFurnaceContainer> {

    public AbstractSmokerFurnaceMenu(String name, Plugin providingPlugin) {
        this(SmokerFurnaceContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractSmokerFurnaceMenu(String name, Plugin providingPlugin, String title) {
        this(SmokerFurnaceContainer.create(null, title), name, providingPlugin);
    }

    public AbstractSmokerFurnaceMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(SmokerFurnaceContainer.create(null, title), name, providingPlugin);
    }

    public AbstractSmokerFurnaceMenu(SmokerFurnaceContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
