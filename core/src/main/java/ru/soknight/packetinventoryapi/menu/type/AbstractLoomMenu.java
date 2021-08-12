package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.annotation.container.PatternSelectListener;
import ru.soknight.packetinventoryapi.container.type.LoomContainer;
import ru.soknight.packetinventoryapi.event.container.PatternSelectEvent;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractLoomMenu extends AbstractMenu<LoomContainer, LoomContainer.LoomUpdateRequest> {

    public AbstractLoomMenu(String name, Plugin providingPlugin) {
        this(LoomContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractLoomMenu(String name, Plugin providingPlugin, String title) {
        this(LoomContainer.create(null, title), name, providingPlugin);
    }

    public AbstractLoomMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(LoomContainer.create(null, title), name, providingPlugin);
    }

    public AbstractLoomMenu(LoomContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    @PatternSelectListener
    public void onPatternSelect(PatternSelectEvent event) {
        runAsync(() -> onPatternSelect(
                event.getActor(), 
                event.getContainer(), 
                event.getSlot(),
                event.getRow(),
                event.getColumn()
        ));
    }

    protected void onPatternSelect(
            Player actor, 
            LoomContainer container,
            int slot,
            int row,
            int column
    ) {}

}
