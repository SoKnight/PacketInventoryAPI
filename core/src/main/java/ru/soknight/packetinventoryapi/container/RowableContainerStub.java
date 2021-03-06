package ru.soknight.packetinventoryapi.container;

import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequestStub;

public interface RowableContainerStub<C extends Container<C, ContentUpdateRequestStub<C>>> {

    int getRowsAmount();

    C setRowsAmount(int amount);

}
