package ru.soknight.packetinventoryapi.menu.container;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;

import java.util.Map;
import java.util.Set;

public interface PublicWrapper<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> PublicWrapper<C, R> wrap(C container) {
        return new SimplePublicWrapper<>(container);
    }

    BaseComponent getTitle();
    PublicWrapper<C, R> setTitle(BaseComponent title);
    PublicWrapper<C, R> updateViewTitles(boolean reopenAll);

    int getRowsAmount();
    PublicWrapper<C, R> setRowsAmount(int amount);

    MenuItem<?, ?> getFiller();
    PublicWrapper<C, R> setFiller(MenuItem<?, ?> filler);

    C getOriginal();

    Set<Player> getViewers();

    Map<Player, C> getViews();

    C getView(Player player);

    DataHolder getDataHolder(Player player);

    boolean open(Player player);

    boolean isViewing(Player player);

    boolean close(Player player);

    void closeAll();

    default void setEventListener(AnyEventListener listener) {}

}
