package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.annotation.container.LecternButtonClickListener;
import ru.soknight.packetinventoryapi.annotation.container.LecternPageOpenListener;
import ru.soknight.packetinventoryapi.container.data.LecternButtonType;
import ru.soknight.packetinventoryapi.container.type.LecternContainer;
import ru.soknight.packetinventoryapi.event.container.LecternButtonClickEvent;
import ru.soknight.packetinventoryapi.event.container.LecternPageOpenEvent;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractLecternMenu extends AbstractMenu<LecternContainer, LecternContainer.LecternUpdateRequest> {

    public AbstractLecternMenu(String name, Plugin providingPlugin) {
        this(LecternContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractLecternMenu(String name, Plugin providingPlugin, String title) {
        this(LecternContainer.create(null, title), name, providingPlugin);
    }

    public AbstractLecternMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(LecternContainer.create(null, title), name, providingPlugin);
    }

    public AbstractLecternMenu(LecternContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    @LecternButtonClickListener
    public void onButtonClick(LecternButtonClickEvent event) {
        runAsync(() -> onButtonClick(
                event.getActor(), 
                event.getContainer(), 
                event.getButtonType()
        ));
    }

    protected void onButtonClick(
            Player actor,
            LecternContainer container,
            LecternButtonType buttonType
    ) {}

    @LecternPageOpenListener
    public void onPageOpen(LecternPageOpenEvent event) {
        runAsync(() -> onPageOpen(
                event.getActor(),
                event.getContainer(),
                event.getPage()
        ));
    }

    protected void onPageOpen(
            Player actor,
            LecternContainer container,
            int page
    ) {}

}
