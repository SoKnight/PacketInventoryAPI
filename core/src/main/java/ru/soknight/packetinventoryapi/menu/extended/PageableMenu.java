package ru.soknight.packetinventoryapi.menu.extended;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.container.type.GenericContainer;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.item.page.element.filler.PageContentFiller;
import ru.soknight.packetinventoryapi.menu.item.page.element.PageElementMenuItem;
import ru.soknight.packetinventoryapi.menu.type.AbstractGenericMenu;

public abstract class PageableMenu<T> extends AbstractGenericMenu {

    protected static final String CURRENT_PAGE_KEY = "current-page";

    public PageableMenu(String name, Plugin providingPlugin, int rows) {
        super(name, providingPlugin, rows);
    }

    public PageableMenu(String name, Plugin providingPlugin, int rows, String title) {
        super(name, providingPlugin, rows, title);
    }

    public PageableMenu(String name, Plugin providingPlugin, int rows, BaseComponent title) {
        super(name, providingPlugin, rows, title);
    }

    public PageableMenu(GenericContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    protected <I extends DisplayableMenuItem> void setPageContentFiller(String itemId, PageContentFiller<I> pageContentFiller) {
        MenuItem menuItem = getMenuItem(itemId);
        if(menuItem == null || !menuItem.isPageElement())
            return;

        PageElementMenuItem<I> pageElementItem = menuItem.asPageElementItem();
        pageElementItem.setPageContentFiller(pageContentFiller);
    }

    public abstract int getElementCount(Player viewer);

    public abstract int getPageSize(Player viewer);

    public int getTotalPages(Player viewer) {
        int count = getElementCount(viewer);
        int pageSize = getPageSize(viewer);

        int pages = count / pageSize;
        if(pages == 0)
            pages++;

        return count % pageSize == 0 ? pages : pages + 1;
    }

    public int getItemStartIndex(Player viewer) {
        return getPageSize(viewer) * (getCurrentPage(viewer) - 1);
    }

    public int getCurrentPage(Player viewer) {
        DataHolder dataHolder = getDataHolder(viewer);
        return dataHolder != null ? dataHolder.getAsInt(CURRENT_PAGE_KEY, 1) : 1;
    }

    public void setCurrentPage(Player viewer, int page) {
        DataHolder dataHolder = getDataHolder(viewer);
        if(dataHolder != null)
            dataHolder.set(CURRENT_PAGE_KEY, page);
    }

    public int getNextPage(Player viewer) { return getCurrentPage(viewer) + 1; }
    public boolean nextPageExist(Player viewer) { return getNextPage(viewer) <= getTotalPages(viewer); }
    public int gotoNextPage(Player viewer) {
        int nextPage = getNextPage(viewer);
        setCurrentPage(viewer, nextPage);
        return nextPage;
    }

    public int getPreviousPage(Player viewer) { return getCurrentPage(viewer) - 1; }
    public boolean previousPageExist(Player viewer) { return getPreviousPage(viewer) >= 1; }
    public int gotoPreviousPage(Player viewer) {
        int previousPage = getPreviousPage(viewer);
        setCurrentPage(viewer, previousPage);
        return previousPage;
    }

}
