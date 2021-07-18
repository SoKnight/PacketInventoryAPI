package ru.soknight.packetinventoryapi.item.update;

import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;

import java.util.Map;

public final class BaseContentUpdateRequestStub<C extends Container<C, ContentUpdateRequestStub<C>>> extends BaseContentUpdateRequest<C, ContentUpdateRequestStub<C>> implements ContentUpdateRequestStub<C> {

    protected BaseContentUpdateRequestStub(C container, Map<Integer, ItemStack> contentData) {
        super(container, contentData);
    }

    protected BaseContentUpdateRequestStub(C container, Map<Integer, ItemStack> contentData, int slotsOffset) {
        super(container, contentData, slotsOffset);
    }

}
