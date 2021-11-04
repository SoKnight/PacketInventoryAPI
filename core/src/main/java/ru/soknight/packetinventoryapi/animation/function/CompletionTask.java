package ru.soknight.packetinventoryapi.animation.function;

import ru.soknight.packetinventoryapi.animation.Animation;

public interface CompletionTask<A extends Animation<A>> {

    void onFinish(A animation);

}
