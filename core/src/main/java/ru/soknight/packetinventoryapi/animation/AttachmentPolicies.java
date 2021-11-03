package ru.soknight.packetinventoryapi.animation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AttachmentPolicies implements AttachmentPolicy {

    AWAIT_COMPLETION(false, false, true),
    AWAIT_NEXT_CYCLE(false, true, true),
    AWAIT_NEXT_TICK(true, true, true),
    FINISH_INSTANTLY(true, true, true);

    private boolean finishOnNextTick;
    private boolean finishOnNextCycle;
    private boolean finishOnCompletion;

    @Override
    public boolean isFinishInstantly() {
        return this == FINISH_INSTANTLY;
    }

}
