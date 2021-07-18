package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.FurnaceContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractFurnaceMenu<F extends FurnaceContainer<F>> extends AbstractMenu<F, FurnaceContainer.FurnaceUpdateRequest<F>> {

    public AbstractFurnaceMenu(F container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    public static abstract class DefaultFurnaceMenu extends AbstractFurnaceMenu<FurnaceContainer.DefaultFurnace> {
        public DefaultFurnaceMenu(String name, Plugin providingPlugin) {
            this((FurnaceContainer.DefaultFurnace) FurnaceContainer.createDefault(null), name, providingPlugin);
        }

        public DefaultFurnaceMenu(String name, Plugin providingPlugin, String title) {
            this((FurnaceContainer.DefaultFurnace) FurnaceContainer.create(null, title), name, providingPlugin);
        }

        public DefaultFurnaceMenu(String name, Plugin providingPlugin, BaseComponent title) {
            this((FurnaceContainer.DefaultFurnace) FurnaceContainer.create(null, title), name, providingPlugin);
        }

        public DefaultFurnaceMenu(FurnaceContainer.DefaultFurnace container, String name, Plugin providingPlugin) {
            super(container, name, providingPlugin);
        }
    }

}
