package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.annotation.container.AnvilRenameListener;
import ru.soknight.packetinventoryapi.container.type.AnvilContainer;
import ru.soknight.packetinventoryapi.event.container.AnvilRenameEvent;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractAnvilMenu extends AbstractMenu<AnvilContainer, AnvilContainer.AnvilUpdateRequest> {

    public AbstractAnvilMenu(String name, Plugin providingPlugin) {
        this(AnvilContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractAnvilMenu(String name, Plugin providingPlugin, String title) {
        this(AnvilContainer.create(null, title), name, providingPlugin);
    }

    public AbstractAnvilMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(AnvilContainer.create(null, title), name, providingPlugin);
    }

    public AbstractAnvilMenu(AnvilContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    @AnvilRenameListener
    public void onRename(AnvilRenameEvent event) {
        runAsync(() -> onRename(event.getActor(), event.getContainer(), event.getCustomName()));
    }

    protected void onRename(Player actor, AnvilContainer container, String customName) {}

}
