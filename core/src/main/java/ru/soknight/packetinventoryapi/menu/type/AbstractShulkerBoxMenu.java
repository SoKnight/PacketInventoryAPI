package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.ShulkerBoxContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenuStub;

public abstract class AbstractShulkerBoxMenu extends AbstractMenuStub<ShulkerBoxContainer> {

    public AbstractShulkerBoxMenu(String name, Plugin providingPlugin) {
        this(ShulkerBoxContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractShulkerBoxMenu(String name, Plugin providingPlugin, String title) {
        this(ShulkerBoxContainer.create(null, title), name, providingPlugin);
    }

    public AbstractShulkerBoxMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(ShulkerBoxContainer.create(null, title), name, providingPlugin);
    }

    public AbstractShulkerBoxMenu(ShulkerBoxContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
