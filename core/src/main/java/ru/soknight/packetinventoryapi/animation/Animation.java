package ru.soknight.packetinventoryapi.animation;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.animation.function.CompletionTask;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public abstract class Animation<A extends Animation<A>> {

    public static final List<Animation<?>> PLAYING_ANIMATIONS = new CopyOnWriteArrayList<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    private static final long FINISH_TIMEOUT = 5000L;

    protected static final int DEFAULT_STEPS_AMOUNT = 1;
    protected static final boolean DEFAULT_VIEW_REQUIRED_FLAG = true;

    @Getter protected final @NotNull Container<?, ?> container;
    @Getter protected final int steps;
    @Getter protected final int cycles;
    @Getter protected final boolean viewRequired;

    @Getter private @Nullable Runnable completionTask;

    protected final AtomicInteger currentTick;
    protected final AtomicInteger currentCycle;

    private Thread thread;
    private boolean playing;

    public Animation(@NotNull Container<?, ?> container, int steps) {
        this(container, steps, DEFAULT_VIEW_REQUIRED_FLAG);
    }

    public Animation(@NotNull Container<?, ?> container, int steps, boolean viewRequired) {
        this(container, steps, DEFAULT_STEPS_AMOUNT, viewRequired);
    }

    public Animation(@NotNull Container<?, ?> container, int steps, int cycles) {
        this(container, steps, cycles, DEFAULT_VIEW_REQUIRED_FLAG);
    }

    public Animation(@NotNull Container<?, ?> container, int steps, int cycles, boolean viewRequired) {
        Validate.notNull(container, "container");
        Validate.isTrue(steps > 0, "'steps' must be more than 0!");
        Validate.isTrue(cycles == -1 || cycles > 0, "'cycles' must be more than 0 or equal -1!");

        this.container = container;
        this.steps = steps;
        this.cycles = cycles;
        this.viewRequired = viewRequired;

        this.currentTick = new AtomicInteger();
        this.currentCycle = new AtomicInteger();
    }

    public static int finishAllSync() {
        CompletableFuture<?>[] futures = new ArrayList<>(PLAYING_ANIMATIONS)
                .stream()
                .map(a -> a.finishAsync(FINISH_TIMEOUT))
                .toArray(CompletableFuture<?>[]::new);

        CompletableFuture.allOf(futures).join();
        return futures.length;
    }

    public static int finishAllSync(Container<?, ?> container) {
        CompletableFuture<?>[] futures = new ArrayList<>(PLAYING_ANIMATIONS)
                .stream()
                .filter(a -> a.isPlayingIn(container))
                .map(a -> a.finishAsync(FINISH_TIMEOUT))
                .toArray(CompletableFuture<?>[]::new);

        CompletableFuture.allOf(futures).join();
        return futures.length;
    }

    public static int finishAllSameTypeSync(Container<?, ?> container, Class<? extends Animation<?>> animationType) {
        CompletableFuture<?>[] futures = new ArrayList<>(PLAYING_ANIMATIONS)
                .stream()
                .filter(a -> a.isPlayingIn(container))
                .filter(a -> a.getClass() == animationType)
                .map(a -> a.finishAsync(FINISH_TIMEOUT))
                .toArray(CompletableFuture<?>[]::new);

        CompletableFuture.allOf(futures).join();
        return futures.length;
    }

    protected abstract A getThis();

    public boolean isPlaying() {
        synchronized (this) {
            return thread != null && thread.isAlive();
        }
    }

    public boolean isPlayingIn(Container<?, ?> container) {
        if(container == null)
            return this.container == null;
        else
            return this.container == container;
    }

    public void playSync() { playSync(0L); }

    @SneakyThrows
    public void playSync(long delay) {
        if(delay > 0L)
            Thread.sleep(delay);

        if(isPlaying())
            return;

        PLAYING_ANIMATIONS.add(this);
        this.playing = true;
        this.thread = new Thread(this::runAll, "PIAPI-Animation-#" + COUNTER.getAndIncrement());
        this.thread.start();

        prePlaySync();
        this.thread.join();
        postPlaySync();

        this.playing = false;
        PLAYING_ANIMATIONS.remove(this);
    }

    protected void prePlaySync() {}

    protected void postPlaySync() {}

    public CompletableFuture<Void> playAsync() {
        return playAsync(null, 0L);
    }

    public CompletableFuture<Void> playAsync(long delay) {
        return playAsync(null, delay);
    }

    public CompletableFuture<Void> playAsync(@Nullable CompletionTask<A> onFinish) {
        return playAsync(onFinish, 0L);
    }

    public CompletableFuture<Void> playAsync(@Nullable CompletionTask<A> onFinish, long delay) {
        return CompletableFuture.runAsync(() -> playSync(delay)).thenRun(() -> {
            if(onFinish != null)
                onFinish.onFinish(getThis());
        });
    }

    public void finishSync() {
        finishSync(0L);
    }

    @SneakyThrows
    public void finishSync(long timeout) {
        if(isPlaying()) {
            synchronized (this) {
                playing = false;
                preFinishSync();
                thread.join(timeout);
                postFinishSync();
            }
            if(completionTask != null)
                completionTask.run();
        }
    }

    protected void preFinishSync() {}

    protected void postFinishSync() {}

    public CompletableFuture<Void> finishAsync() {
        return finishAsync(0L);
    }

    public CompletableFuture<Void> finishAsync(long timeout) {
        return finishAsync(timeout, null);
    }

    public CompletableFuture<Void> finishAsync(long timeout, Consumer<A> onFinish) {
        return CompletableFuture.runAsync(() -> finishSync(timeout)).thenRun(() -> {
            if(onFinish != null)
                onFinish.accept(getThis());
        });
    }

    public A setCompletionTask(@NotNull Runnable completionTask) {
        Validate.notNull(completionTask, "completionTask");
        this.completionTask = completionTask;
        return getThis();
    }

    private void runAll() {
        if(isViewRequired() && !container.isViewing())
            return;

        while(isPlaying() && (cycles == -1 || currentCycle.get() < cycles) && canRunNextCycle())
            runCycle(currentCycle.getAndIncrement());

        currentCycle.set(0);
    }

    private void runCycle(int cycle) {
        while(isPlaying() && currentTick.get() < steps && canRunNextTick())
            tickQuietly(cycle, currentTick.getAndIncrement());

        currentTick.set(0);
    }

    protected boolean canRunNextTick() { return true; }

    protected boolean canRunNextCycle() { return true; }

    private void tickQuietly(int cycle, int step) {
        try {
            tick(cycle, step);
        } catch (Throwable ignored) {}
    }

    protected abstract void tick(int cycle, int step) throws Throwable;

}
