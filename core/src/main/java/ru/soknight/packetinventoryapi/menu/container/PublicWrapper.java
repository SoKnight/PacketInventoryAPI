package ru.soknight.packetinventoryapi.menu.container;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;

import java.util.Map;
import java.util.Set;

public interface PublicWrapper<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> @NotNull PublicWrapper<C, R> wrap(@NotNull C container) {
        return new SimplePublicWrapper<>(container);
    }

    @NotNull BaseComponent getTitle();
    @NotNull PublicWrapper<C, R> setTitle(@NotNull BaseComponent title);
    @NotNull PublicWrapper<C, R> updateViewTitles(boolean reopenAll);

    int getRowsAmount();
    @NotNull PublicWrapper<C, R> setRowsAmount(int amount);

    @Nullable DisplayableMenuItem getFiller();
    @NotNull PublicWrapper<C, R> setFiller(@Nullable DisplayableMenuItem filler);

    @NotNull C getOriginal();

    @NotNull @UnmodifiableView Set<Player> getViewers();

    @NotNull @UnmodifiableView Map<Player, C> getViews();

    @Nullable C getView(@NotNull Player player);

    @Nullable DataHolder getDataHolder(@NotNull Player player);

    boolean open(@NotNull Player player);

    boolean isViewing(@NotNull Player player);

    boolean close(@NotNull Player player);

    void closeAll();

    default void setEventListener(@NotNull AnyEventListener listener) {}

}
