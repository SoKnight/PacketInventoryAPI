package ru.soknight.packetinventoryapi.menu.item.page.element;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.item.page.element.filler.PageContentFiller;

public interface PageElementMenuItem<I extends DisplayableMenuItem> extends MenuItem {

    static <I extends DisplayableMenuItem> @NotNull PageElementMenuItem<I> create(
            @NotNull ConfigurationSection configuration,
            @NotNull I elementPattern
    ) {
        return new SimplePageElementMenuItem<>(configuration, elementPattern);
    }

    @NotNull I getElementPattern();

    @Nullable PageContentFiller<I> getPageContentFiller();

    @NotNull PageElementMenuItem<I> setPageContentFiller(@Nullable PageContentFiller<I> pageContentFiller);

}
