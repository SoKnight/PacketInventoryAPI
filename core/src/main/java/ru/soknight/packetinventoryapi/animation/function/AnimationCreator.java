package ru.soknight.packetinventoryapi.animation.function;

import ru.soknight.packetinventoryapi.animation.Animation;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;

@FunctionalInterface
public interface AnimationCreator<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>, A extends Animation<A>> {

    A create(C container);

}
