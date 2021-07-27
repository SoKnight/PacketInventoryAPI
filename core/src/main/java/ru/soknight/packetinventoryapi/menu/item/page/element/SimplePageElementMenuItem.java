package ru.soknight.packetinventoryapi.menu.item.page.element;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.page.element.filler.PageContentFiller;

@Getter
final class SimplePageElementMenuItem<I extends DisplayableMenuItem> implements PageElementMenuItem<I> {

    private final ConfigurationSection configuration;
    private final I elementPattern;
    private PageContentFiller<I> pageContentFiller;

    SimplePageElementMenuItem(ConfigurationSection configuration, I elementPattern) {
        this.configuration = configuration;
        this.elementPattern = elementPattern;
    }

    @Override
    public @NotNull PageElementMenuItem<I> setPageContentFiller(PageContentFiller<I> pageContentFiller) {
        this.pageContentFiller = pageContentFiller;
        return this;
    }

}
