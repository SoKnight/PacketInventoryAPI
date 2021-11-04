package ru.soknight.packetinventoryapi.animation.function;

import ru.soknight.packetinventoryapi.animation.AttachableAnimation;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;

import java.util.concurrent.CompletableFuture;

public interface AttachableCreator<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>, A extends AttachableAnimation<A, T>, T> {

    A create(C container, CompletableFuture<T> task);

}
