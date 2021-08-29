package ru.soknight.packetinventoryapi.container;

import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;

public interface RowableContainer<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    int getRowsAmount();

    C setRowsAmount(int amount);

}
