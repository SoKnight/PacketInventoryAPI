package ru.soknight.packetinventoryapi.container;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequestStub;

public abstract class ContainerStub<C extends Container<C, ContentUpdateRequestStub<C>>> extends Container<C, ContentUpdateRequestStub<C>> {

    protected ContainerStub(Player inventoryHolder, ContainerType containerType, String title) {
        super(inventoryHolder, containerType, title);
    }

    protected ContainerStub(Player inventoryHolder, ContainerType containerType, BaseComponent title) {
        super(inventoryHolder, containerType, title);
    }

    @Override
    public ContentUpdateRequestStub<C> updateContent() {
        return ContentUpdateRequest.create(getThis(), contentData);
    }

}
