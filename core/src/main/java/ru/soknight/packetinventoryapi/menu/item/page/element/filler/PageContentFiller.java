package ru.soknight.packetinventoryapi.menu.item.page.element.filler;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.page.element.renderer.SlotItemRenderer;
import ru.soknight.packetinventoryapi.placeholder.element.ElementPlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.element.LiteElementPlaceholderReplacer;

public interface PageContentFiller<I extends DisplayableMenuItem> {

    static <I extends DisplayableMenuItem> PageContentFiller<I> create() {
        return create(true);
    }

    static <I extends DisplayableMenuItem> PageContentFiller<I> create(boolean replaceWithEmptyItems) {
        return create(SlotItemRenderer.DEFAULT_CLONING, replaceWithEmptyItems);
    }

    static <I extends DisplayableMenuItem> PageContentFiller<I> create(@Nullable SlotItemRenderer slotItemRenderer) {
        return create(slotItemRenderer, true);
    }

    static <I extends DisplayableMenuItem> PageContentFiller<I> create(
            @Nullable SlotItemRenderer slotItemRenderer,
            boolean replaceWithEmptyItems
    ) {
        return new SimplePageContentFiller<>(slotItemRenderer, replaceWithEmptyItems);
    }

    void fillPageContent(@NotNull Player viewer, @NotNull I menuItem, int startIndex, @NotNull ContentUpdateRequest<?, ?> contentUpdateRequest);

    PageContentFiller<I> appendReplacerFirst(@NotNull LiteElementPlaceholderReplacer replacer);

    PageContentFiller<I> appendReplacerFirst(@NotNull ElementPlaceholderReplacer replacer);

    PageContentFiller<I> appendReplacer(@NotNull LiteElementPlaceholderReplacer replacer);

    PageContentFiller<I> appendReplacer(@NotNull ElementPlaceholderReplacer replacer);

    PageContentFiller<I> removeReplacer(@NotNull ElementPlaceholderReplacer replacer);

}
