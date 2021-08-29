package ru.soknight.packetinventoryapi.menu.container;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
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

    SimplePublicWrapper(C original) {
        this(original, null);
    }

    SimplePublicWrapper(C original, AnyEventListener eventListener) {
        this.original = original;
        this.eventListener = eventListener;
        this.views = new ConcurrentHashMap<>();
    }

    @Override
    public BaseComponent getTitle() {
        return original.getTitle();
    }

    @Override
    public PublicWrapper<C, R> setTitle(BaseComponent title) {
        this.original.setTitle(title);
        return this;
    }

    @Override
    public PublicWrapper<C, R> updateViewTitles(boolean reopenAll) {
        views.values().forEach(view -> {
            view.setTitle(getTitle());
            if(reopenAll)
                view.reopen();
        });
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getRowsAmount() {
        if(original instanceof RowableContainer<?, ?>)
            return ((RowableContainer<C, R>) original).getRowsAmount();

        throw new UnsupportedOperationException(String.format(
                "container type %s is not a RowableContainer!",
                original.getClass().getSimpleName()
        ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public PublicWrapper<C, R> setRowsAmount(int amount) {
        if(original instanceof RowableContainer<?, ?>) {
            ((RowableContainer<C, R>) original).setRowsAmount(amount);
            return this;
        }

        throw new UnsupportedOperationException(String.format(
                "container type %s is not a RowableContainer!",
                original.getClass().getSimpleName()
        ));
    }

    @Override
    public DisplayableMenuItem getFiller() {
        return original.getFiller();
    }

    @Override
    public PublicWrapper<C, R> setFiller(DisplayableMenuItem filler) {
        original.setFiller(filler);
        return this;
    }

    @Override
    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(views.keySet());
    }

    @Override
    public Map<Player, C> getViews() {
        return Collections.unmodifiableMap(views);
    }

    @Override
    public C getView(Player player) {
        return views.get(player);
    }

    @Override
    public DataHolder getDataHolder(Player player) {
        C view = getView(player);
        return view != null ? view.getDataHolder() : null;
    }

    @Override
    public boolean open(Player player) {
        if(isViewing(player))
            return false;

        C clone = original.copyFull(player, eventListener);
        views.put(player, clone);
        clone.open();
        return true;
    }

    @Override
    public boolean isViewing(Player player) {
        return views.containsKey(player);
    }

    @Override
    public boolean close(Player player) {
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

    private boolean closeSafety(Player player) {
        C container = views.get(player);
        if(container != null)
            container.close();

        return true;
    }

    @Override
    public void setEventListener(AnyEventListener eventListener) {
        this.eventListener = eventListener;
    }

}
