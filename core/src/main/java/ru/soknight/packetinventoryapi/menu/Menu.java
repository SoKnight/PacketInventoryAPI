package ru.soknight.packetinventoryapi.menu;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.menu.container.PublicWrapper;

public interface Menu<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    PublicWrapper<C, R> getContainer();

    String getName();

    Plugin getProvidingPlugin();

    boolean open(Player player);

    boolean isViewing(Player player);

    boolean close(Player player);

    void closeAll();

}
