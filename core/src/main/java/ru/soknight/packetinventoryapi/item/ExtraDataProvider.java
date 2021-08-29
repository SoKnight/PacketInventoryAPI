package ru.soknight.packetinventoryapi.item;

import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;

@FunctionalInterface
public interface ExtraDataProvider<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    void provideExtraData(ContentUpdateRequest<C, R> contentUpdateRequest);

}
