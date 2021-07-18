package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.type.CraftingTableContainer;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractCraftingTableMenu extends AbstractMenu<CraftingTableContainer, CraftingTableContainer.CraftingTableUpdateRequest> {

    public AbstractCraftingTableMenu(String name, Plugin providingPlugin) {
        this(CraftingTableContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractCraftingTableMenu(String name, Plugin providingPlugin, String title) {
        this(CraftingTableContainer.create(null, title), name, providingPlugin);
    }

    public AbstractCraftingTableMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(CraftingTableContainer.create(null, title), name, providingPlugin);
    }

    public AbstractCraftingTableMenu(CraftingTableContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

}
