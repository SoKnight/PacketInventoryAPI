package ru.soknight.packetinventoryapi.animation;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.Validate;
import ru.soknight.packetinventoryapi.container.Container;

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

    @Getter protected final Container<?, ?> container;
    @Getter protected final int steps;
    @Getter protected final int cycles;
    @Getter protected final boolean viewRequired;

    protected final AtomicInteger currentTick;
    protected final AtomicInteger currentCycle;

    private Thread thread;
    private boolean playing;

    public Animation(Container<?, ?> container, int steps) {
        this(container, steps, 1);
    }

    public Animation(Container<?, ?> container, int steps, boolean viewRequired) {
        this(container, steps, 1, viewRequired);
    }

    public Animation(Container<?, ?> container, int steps, int cycles) {
        this(container, steps, cycles, true);
    }

    public Animation(Container<?, ?> container, int steps, int cycles, boolean viewRequired) {
        Validate.notNull(container, "'container' cannot be null!");
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

    protected abstract A getThis();

    public boolean isPlaying() {
        return thread != null && thread.isAlive();
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
        this.thread.join();

        this.playing = false;
        PLAYING_ANIMATIONS.remove(this);
    }

    public CompletableFuture<Void> playAsync(Consumer<A> onFinish) {
        return CompletableFuture.runAsync(this::playSync).thenRun(() -> {
            if(onFinish != null)
                onFinish.accept(getThis());
        });
    }

    public CompletableFuture<Void> playAsync(Consumer<A> onFinish, long delay) {
        return CompletableFuture.runAsync(() -> playSync(delay)).thenRun(() -> {
            if(onFinish != null)
                onFinish.accept(getThis());
        });
    }

    public void finishSync() {
        finishSync(0L);
    }

    @SneakyThrows
    public void finishSync(long timeout) {
        if(isPlaying()) {
            playing = false;
            thread.join(timeout);
        }
    }

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

    private void runAll() {
        if(isViewRequired() && !container.isViewing())
            return;

        while(playing && (cycles == -1 || currentCycle.get() < cycles))
            runCycle(currentCycle.getAndIncrement());

        currentCycle.set(0);
    }

    private void runCycle(int cycle) {
        while(playing && currentTick.get() < steps)
            tickQuietly(cycle, currentTick.getAndIncrement());

        currentTick.set(0);
    }

    private void tickQuietly(int cycle, int step) {
        try {
            tick(cycle, step);
        } catch (Throwable ignored) {}
    }

    protected abstract void tick(int cycle, int step) throws Throwable;

}
