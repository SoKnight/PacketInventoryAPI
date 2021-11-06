package ru.soknight.packetinventoryapi.menu.container;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.api.PacketInventoryAPI;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.RowableContainer;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
final class SimplePublicWrapper<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> implements PublicWrapper<C, R> {

    private final C original;
    private final Map<Player, C> views;
    private AnyEventListener eventListener;

    SimplePublicWrapper(@NotNull C original) {
        this(original, null);
    }

    SimplePublicWrapper(@NotNull C original, @NotNull AnyEventListener eventListener) {
        this.original = original;
        this.eventListener = eventListener;
        this.views = new ConcurrentHashMap<>();
    }

    @Override
    public @NotNull BaseComponent getTitle() {
        return original.getTitle();
    }

    @Override
    public @NotNull PublicWrapper<C, R> setTitle(@NotNull BaseComponent title) {
        this.original.setTitle(title);
        return this;
    }

    @Override
    public @NotNull PublicWrapper<C, R> updateViewTitles(boolean reopenAll) {
        views.values().forEach(view -> {
            view.setTitle(getTitle());
            if(reopenAll)
                view.reopen();
        });
        return this;
    }

    @Override
    public int getRowsAmount() {
        if(original instanceof RowableContainer<?, ?>)
            return ((RowableContainer<?, ?>) original).getRowsAmount();

        throw new UnsupportedOperationException(String.format(
                "container type %s is not a RowableContainer!",
                original.getClass().getSimpleName()
        ));
    }

    @Override
    public @NotNull PublicWrapper<C, R> setRowsAmount(int amount) {
        if(original instanceof RowableContainer<?, ?>) {
            ((RowableContainer<?, ?>) original).setRowsAmount(amount);
            return this;
        }

        throw new UnsupportedOperationException(String.format(
                "container type %s is not a RowableContainer!",
                original.getClass().getSimpleName()
        ));
    }

    @Override
    public @Nullable DisplayableMenuItem getFiller() {
        return original.getFiller();
    }

    @Override
    public @NotNull PublicWrapper<C, R> setFiller(@Nullable DisplayableMenuItem filler) {
        original.setFiller(filler);
        return this;
    }

    @Override
    public @NotNull @UnmodifiableView Set<Player> getViewers() {
        return Collections.unmodifiableSet(views.keySet());
    }

    @Override
    public @NotNull @UnmodifiableView Map<Player, C> getViews() {
        return Collections.unmodifiableMap(views);
    }

    @Override
    public @Nullable C getView(@NotNull Player player) {
        return views.get(player);
    }

    @Override
    public @Nullable DataHolder getDataHolder(@NotNull Player player) {
        C view = getView(player);
        return view != null ? view.getDataHolder() : null;
    }

    @Override
    public boolean open(@NotNull Player player) {
        if(isViewing(player))
            return false;

        C clone = original.copyFull(player, eventListener);
        views.put(player, clone);
        clone.open();
        return true;
    }

    @Override
    public boolean isViewing(@NotNull Player player) {
        C container = views.get(player);
        if(container == null)
            return false;

        return PacketInventoryAPI.getInstance().containerStorage().isViewing(container);
    }

    @Override
    public boolean close(@NotNull Player player) {
        C container = views.remove(player);
        if(container == null)
            return false;

        container.close();
        return true;
    }

    @Override
    public void closeAll() {
        views.keySet().removeIf(this::closeSafety);
    }

    private boolean closeSafety(@NotNull Player player) {
        C container = views.get(player);
        if(container != null)
            container.close();

        return true;
    }

    @Override
    public void setEventListener(@NotNull AnyEventListener eventListener) {
        this.eventListener = eventListener;
    }

}
