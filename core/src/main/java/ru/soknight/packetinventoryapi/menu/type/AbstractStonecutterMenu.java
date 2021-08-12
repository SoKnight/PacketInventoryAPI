package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.annotation.container.RecipeSelectListener;
import ru.soknight.packetinventoryapi.container.type.StonecutterContainer;
import ru.soknight.packetinventoryapi.event.container.RecipeSelectEvent;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractStonecutterMenu extends AbstractMenu<StonecutterContainer, StonecutterContainer.StonecutterUpdateRequest> {

    public AbstractStonecutterMenu(String name, Plugin providingPlugin) {
        this(StonecutterContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractStonecutterMenu(String name, Plugin providingPlugin, String title) {
        this(StonecutterContainer.create(null, title), name, providingPlugin);
    }

    public AbstractStonecutterMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(StonecutterContainer.create(null, title), name, providingPlugin);
    }

    public AbstractStonecutterMenu(StonecutterContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    @RecipeSelectListener
    public void onRecipeSelect(RecipeSelectEvent event) {
        onRecipeSelect(
                event.getActor(), 
                event.getContainer(), 
                event.getSlot(),
                event.getRow(),
                event.getColumn()
        );
    }

    protected void onRecipeSelect(
            Player actor, 
            StonecutterContainer container,
            int slot,
            int row,
            int column
    ) {}

}
