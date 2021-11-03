package ru.soknight.packetinventoryapi.animation;

public interface AttachmentPolicy {

    boolean isFinishOnNextTick();

    boolean isFinishOnNextCycle();

    boolean isFinishOnCompletion();

    boolean isFinishInstantly();

}
