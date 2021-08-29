package ru.soknight.packetinventoryapi.item.update.content;

import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;

import java.util.Map;

public final class BaseContentUpdateRequestStub<C extends Container<C, ContentUpdateRequestStub<C>>> extends BaseContentUpdateRequest<C, ContentUpdateRequestStub<C>> implements ContentUpdateRequestStub<C> {

    BaseContentUpdateRequestStub(C container, Map<Integer, ItemStack> contentData) {
        super(container, contentData);
    }

    BaseContentUpdateRequestStub(C container, Map<Integer, ItemStack> contentData, int slotsOffset) {
        super(container, contentData, slotsOffset);
    }

}
