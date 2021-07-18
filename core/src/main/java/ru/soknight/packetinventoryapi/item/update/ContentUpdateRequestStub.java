package ru.soknight.packetinventoryapi.item.update;

import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;

import java.util.Map;

public interface ContentUpdateRequestStub<C extends Container<C, ContentUpdateRequestStub<C>>> extends ContentUpdateRequest<C, ContentUpdateRequestStub<C>> {

    static <C extends Container<C, ContentUpdateRequestStub<C>>> ContentUpdateRequestStub<C> create(C container, Map<Integer, ItemStack> contentData) {
        return new BaseContentUpdateRequestStub<>(container, contentData);
    }

    static <C extends Container<C, ContentUpdateRequestStub<C>>> ContentUpdateRequestStub<C> create(C container, Map<Integer, ItemStack> contentData, int slotsOffset) {
        return new BaseContentUpdateRequestStub<>(container, contentData, slotsOffset);
    }

}
