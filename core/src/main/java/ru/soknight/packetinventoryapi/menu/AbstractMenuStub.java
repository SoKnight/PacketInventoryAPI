package ru.soknight.packetinventoryapi.menu;

import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequestStub;
import ru.soknight.packetinventoryapi.menu.container.PublicWrapper;

public abstract class AbstractMenuStub<C extends Container<C, ContentUpdateRequestStub<C>>> extends AbstractMenu<C, ContentUpdateRequestStub<C>> {
    protected AbstractMenuStub(PublicWrapper<C, ContentUpdateRequestStub<C>> container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    protected AbstractMenuStub(C container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }
}
