package ru.soknight.packetinventoryapi.animation;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.animation.function.TaskFailureHandler;
import ru.soknight.packetinventoryapi.animation.function.TaskResultHandler;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.concurrent.CompletableFuture;

@Getter
public abstract class AttachableAnimation<A extends AttachableAnimation<A, T>, T> extends Animation<A> {

    protected final @NotNull CompletableFuture<T> task;
    private @NotNull AttachmentPolicy attachmentPolicy;
    private @NotNull TaskResultHandler<T> resultHandler;
    private @NotNull TaskFailureHandler failureHandler;
    private boolean finishAnimationSynchronously;

    @Getter(AccessLevel.NONE) private T taskResult;
    @Getter(AccessLevel.NONE) private Throwable taskThrowable;

    public AttachableAnimation(@NotNull Container<?, ?> container, @NotNull CompletableFuture<T> task, int steps) {
        this(container, task, steps, DEFAULT_VIEW_REQUIRED_FLAG);
    }

    public AttachableAnimation(@NotNull Container<?, ?> container, @NotNull CompletableFuture<T> task, int steps, boolean viewRequired) {
        this(container, task, steps, DEFAULT_STEPS_AMOUNT, viewRequired);
    }

    public AttachableAnimation(@NotNull Container<?, ?> container, @NotNull CompletableFuture<T> task, int steps, int cycles) {
        this(container, task, steps, cycles, DEFAULT_VIEW_REQUIRED_FLAG);
    }

    public AttachableAnimation(@NotNull Container<?, ?> container, @NotNull CompletableFuture<T> task, int steps, int cycles, boolean viewRequired) {
        super(container, steps, cycles, viewRequired);
        Validate.notNull(task, "task");
        this.task = task;

        this.attachmentPolicy = AttachmentPolicies.FINISH_INSTANTLY;
        this.resultHandler = (player, result) -> {};
        this.failureHandler = (player, throwable) -> {};
        this.finishAnimationSynchronously = false;
    }

    @Override
    protected void prePlaySync() {
        task.whenComplete((result, throwable) -> {
            synchronized (AttachableAnimation.this) {
                this.taskResult = result;
                this.taskThrowable = throwable;
            }

            if(attachmentPolicy.isFinishInstantly())
                finishAnimationPlaying();
        });
    }

    @Override
    protected void postFinishSync() {
        if(attachmentPolicy.isFinishOnCompletion())
            handleTaskResult();
    }

    private void finishAnimationPlaying() {
        if(finishAnimationSynchronously) {
            finishSync();
        } else {
            finishAsync();
        }
    }

    private void handleTaskResult() {
        Player player = container.getInventoryHolder();
        if(taskThrowable == null) {
            resultHandler.handle(player, taskResult);
        } else {
            failureHandler.handle(player, taskThrowable);
        }
    }

    @Override
    protected boolean canRunNextTick() {
        return checkCanRunWithAdditionalCondition(!attachmentPolicy.isFinishOnNextTick());
    }

    @Override
    protected boolean canRunNextCycle() {
        return checkCanRunWithAdditionalCondition(!attachmentPolicy.isFinishOnNextCycle());
    }

    private boolean checkCanRunWithAdditionalCondition(boolean additionalCondition) {
        synchronized (this) {
            if(!isTaskDone() || additionalCondition)
                return true;

            finishAnimationPlaying();
            return false;
        }
    }

    public A setAttachmentPolicy(@NotNull AttachmentPolicy attachmentPolicy) {
        Validate.notNull(attachmentPolicy, "attachmentPolicy");
        this.attachmentPolicy = attachmentPolicy;
        return getThis();
    }

    public A setTaskResultHandler(@NotNull TaskResultHandler<T> resultHandler) {
        Validate.notNull(resultHandler, "resultHandler");
        this.resultHandler = resultHandler;
        return getThis();
    }

    public A setTaskFailureHandler(@NotNull TaskFailureHandler failureHandler) {
        Validate.notNull(failureHandler, "failureHandler");
        this.failureHandler = failureHandler;
        return getThis();
    }

    public A setFinishAnimationSynchronously(boolean finishAnimationSynchronously) {
        this.finishAnimationSynchronously = finishAnimationSynchronously;
        return getThis();
    }

    public boolean isTaskDone() {
        return task.isDone();
    }

    public boolean isTaskCompleted() {
        return isTaskDone() && !task.isCompletedExceptionally();
    }

    public boolean isTaskFailure() {
        return isTaskDone() && task.isCompletedExceptionally();
    }

}
