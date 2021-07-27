package ru.soknight.packetinventoryapi.menu.item.page.element.filler;

import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.page.element.renderer.SlotItemRenderer;

final class SimplePageContentFiller<I extends DisplayableMenuItem> extends AbstractPageContentFiller<I> {

    SimplePageContentFiller(SlotItemRenderer slotItemRenderer, boolean replaceWithEmptyItems) {
        super(slotItemRenderer, replaceWithEmptyItems);
    }

}
