package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.BlastFurnaceContainer;

public abstract class AbstractBlastFurnaceMenu extends AbstractFurnaceMenu<BlastFurnaceContainer> {

    public AbstractBlastFurnaceMenu(String name, Plugin providingPlugin) {
        this(BlastFurnaceContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractBlastFurnaceMenu(String name, Plugin providingPlugin, String title) {
        this(BlastFurnaceContainer.create(null, title), name, providingPlugin);
    }

    public AbstractBlastFurnaceMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(BlastFurnaceContainer.create(null, title), name, providingPlugin);
    }

    public AbstractBlastFurnaceMenu(BlastFurnaceContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
