package ru.soknight.packetinventoryapi.menu.registry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.annotation.container.*;
import ru.soknight.packetinventoryapi.annotation.window.ClickListener;
import ru.soknight.packetinventoryapi.annotation.window.CloseListener;
import ru.soknight.packetinventoryapi.annotation.window.ContentLoadListener;
import ru.soknight.packetinventoryapi.annotation.window.OpenListener;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.data.EnchantmentPosition;
import ru.soknight.packetinventoryapi.container.data.LecternButtonType;
import ru.soknight.packetinventoryapi.container.type.*;
import ru.soknight.packetinventoryapi.event.Event;
import ru.soknight.packetinventoryapi.event.container.*;
import ru.soknight.packetinventoryapi.event.window.WindowClickEvent;
import ru.soknight.packetinventoryapi.event.window.WindowCloseEvent;
import ru.soknight.packetinventoryapi.event.window.WindowContentLoadEvent;
import ru.soknight.packetinventoryapi.event.window.WindowOpenEvent;
import ru.soknight.packetinventoryapi.exception.menu.InvalidMethodStructureException;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.menu.Menu;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

final class RegistrationBundle<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    private static final BiPredicate<? extends Annotation, ? extends Event<?, ?>> DEFAULT_PREDICATE;
    private static final Map<Class<? extends Annotation>, BiPredicate<? extends Annotation, ? extends Event<?, ?>>> PREDICATES;

    @Getter
    private final Menu<C, R> menu;

    private final List<Listener> windowOpenListeners;
    private final List<Listener> windowContentLoadListeners;
    private final List<Listener> windowClickListeners;
    private final List<Listener> windowCloseListeners;

    private final List<Listener> anvilRenameListeners;
    private final List<Listener> beaconEffectChangeListeners;
    private final List<Listener> enchantmentSelectListeners;
    private final List<Listener> lecternButtonClickListeners;
    private final List<Listener> lecternPageOpenListeners;
    private final List<Listener> patternSelectListeners;
    private final List<Listener> recipeSelectListeners;
    private final List<Listener> tradeSelectListeners;

    RegistrationBundle(Menu<C, R> menu) {
        this.menu = menu;

        this.windowOpenListeners = new ArrayList<>();
        this.windowContentLoadListeners = new ArrayList<>();
        this.windowClickListeners = new ArrayList<>();
        this.windowCloseListeners = new ArrayList<>();

        this.anvilRenameListeners = new ArrayList<>();
        this.beaconEffectChangeListeners = new ArrayList<>();
        this.enchantmentSelectListeners = new ArrayList<>();
        this.lecternButtonClickListeners = new ArrayList<>();
        this.lecternPageOpenListeners = new ArrayList<>();
        this.patternSelectListeners = new ArrayList<>();
        this.recipeSelectListeners = new ArrayList<>();
        this.tradeSelectListeners = new ArrayList<>();
    }

    IdentifyTicket getTicket() {
        return IdentifyTicket.create(menu);
    }

    boolean isViewing(Player player) {
        return menu.isViewing(player);
    }

    boolean isRepresents(Menu<?, ?> menu) {
        return Objects.equals(getTicket(), IdentifyTicket.create(menu));
    }

    void fireEvent(Event<?, ?> event) {
        event.setContainer(menu.getContainer().getView(event.getActor()));

        if(event instanceof WindowCloseEvent<?, ?>) {
            if(menu.isViewing(event.getActor()))
                menu.close(event.getActor());
            else
                return;
        }

        // --- general window events
        if(event instanceof WindowOpenEvent<?, ?>)
            windowOpenListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof WindowContentLoadEvent<?, ?>)
            windowContentLoadListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof WindowClickEvent<?, ?>)
            windowClickListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof WindowCloseEvent<?, ?>)
            windowCloseListeners.forEach(listener -> listener.invoke(event));

        // --- specific events
        else if(event instanceof AnvilRenameEvent)
            anvilRenameListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof BeaconEffectChangeEvent)
            beaconEffectChangeListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof EnchantmentSelectEvent)
            enchantmentSelectListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof LecternButtonClickEvent)
            lecternButtonClickListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof LecternPageOpenEvent)
            lecternPageOpenListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof PatternSelectEvent)
            patternSelectListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof RecipeSelectEvent)
            recipeSelectListeners.forEach(listener -> listener.invoke(event));
        else if(event instanceof TradeSelectEvent)
            tradeSelectListeners.forEach(listener -> listener.invoke(event));
    }

    void registerListeners() throws InvalidMethodStructureException {
        C container = menu.getContainer().getOriginal();

        for(Method method : menu.getClass().getMethods()) {
            // --- window events
            registerListener(method, OpenListener.class, WindowOpenEvent.class, windowOpenListeners);
            registerListener(method, ContentLoadListener.class, WindowContentLoadEvent.class, windowContentLoadListeners);
            registerListener(method, ClickListener.class, WindowClickEvent.class, windowClickListeners);
            registerListener(method, CloseListener.class, WindowCloseEvent.class, windowCloseListeners);

            // --- container events
            if(container instanceof AnvilContainer)
                registerListener(method, AnvilRenameListener.class, AnvilRenameEvent.class, anvilRenameListeners);

            if(container instanceof BeaconContainer)
                registerListener(method, BeaconEffectChangeListener.class, BeaconEffectChangeEvent.class, beaconEffectChangeListeners);

            if(container instanceof EnchantmentTableContainer)
                registerListener(method, EnchantmentSelectListener.class, EnchantmentSelectEvent.class, enchantmentSelectListeners);

            if(container instanceof LecternContainer) {
                registerListener(method, LecternButtonClickListener.class, LecternButtonClickEvent.class, lecternButtonClickListeners);
                registerListener(method, LecternPageOpenListener.class, LecternPageOpenEvent.class, lecternPageOpenListeners);
            }

            if(container instanceof LoomContainer)
                registerListener(method, PatternSelectListener.class, PatternSelectEvent.class, patternSelectListeners);

            if(container instanceof MerchantContainer)
                registerListener(method, TradeSelectListener.class, TradeSelectEvent.class, tradeSelectListeners);

            if(container instanceof StonecutterContainer)
                registerListener(method, RecipeSelectListener.class, RecipeSelectEvent.class, recipeSelectListeners);
        }
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation, E extends Event<?, ?>> void registerListener(
            Method method,
            Class<A> annotationClass,
            Class<E> eventClass,
            List<Listener> list
    ) throws InvalidMethodStructureException {
        A annotation = getAnnotation(method, annotationClass, eventClass);
        if(annotation == null)
            return;

        BiPredicate<A, E> predicate = (BiPredicate<A, E>) PREDICATES.getOrDefault(annotationClass, DEFAULT_PREDICATE);
        Predicate<E> singlePredicate = event -> predicate.test(annotation, event);

        list.add(createListener(method, singlePredicate));
    }

    private <A extends Annotation, E extends Event<?, ?>> A getAnnotation(
            Method method,
            Class<A> annotationClass,
            Class<E> eventClass
    ) throws InvalidMethodStructureException {
        A annotation = method.getDeclaredAnnotation(annotationClass);
        if(annotation == null)
            return annotation;

        Class<?>[] parameterTypes = method.getParameterTypes();
        if(parameterTypes.length != 1)
            throw new InvalidMethodStructureException(method, eventClass);

        Class<?> parameterType = parameterTypes[0];
        if(parameterType != eventClass)
            throw new InvalidMethodStructureException(method, eventClass);

        return annotation;
    }

    @SuppressWarnings("unchecked")
    private <E extends Event<?, ?>> Listener createListener(Method method, Predicate<E> predicate) {
        return new Listener(method, (Predicate<Event<?, ?>>) predicate);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private final class Listener {
        private final Method methodHandle;
        private final Predicate<Event<?, ?>> predicate;

        public Listener(Method methodHandle) {
            this(methodHandle, e -> true);
        }

        @SneakyThrows
        private void invoke(Event<?, ?> event) {
            if(predicate.test(event))
                methodHandle.invoke(menu, event);
        }
    }

    private static <A extends Annotation, E extends Event<?, ?>> void addPredicate(
            Class<A> annotationClass,
            Class<E> eventClass,
            BiPredicate<A, E> predicate
    ) {
        PREDICATES.put(annotationClass, predicate);
    }

    private static Set<Integer> extractFilteringSlots(ClickListener listener) {
        Set<Integer> output = new HashSet<>();

        int[] slots = listener.value();
        if(slots != null && slots.length != 0)
            for(int slot : slots)
                output.add(slot);

        int startSlot = listener.startSlot();
        int destSlot = listener.destSlot();

        if(startSlot >= 0 && destSlot >= 0)
            output.addAll(new IntRange(startSlot, destSlot).itemsSet());

        return output;
    }

    private static <T> boolean contains(T[] array, T item) {
        for(T element : array)
            if(element == item)
                return true;

        return false;
    }

    private static boolean contains(int[] array, int item) {
        for(int element : array)
            if(element == item)
                return true;

        return false;
    }

    static {
        DEFAULT_PREDICATE = (listener, event) -> true;
        PREDICATES = new HashMap<>();

        addPredicate(
                OpenListener.class,
                WindowOpenEvent.class,
                (listener, event) -> listener.ignoreReopening() || !event.isReopened()
        );

        addPredicate(
                ClickListener.class,
                WindowClickEvent.class,
                (listener, event) -> {
                    Container<?, ?> container = event.getContainer();
                    int slot = event.getClickedSlot();

                    if(slot == -999 && listener.ignoreOutsideClick())
                        return false;

                    // it's an inventory slot?
                    if(listener.includeInventory() && container.playerInventorySlots().contains(slot))
                        return true;

                    // it's a hotbar slot?
                    if(listener.includeHotbar() && container.playerHotbarSlots().contains(slot))
                        return true;

                    // manual checking
                    Set<Integer> slots = extractFilteringSlots(listener);
                    return slots.isEmpty() || slots.contains(slot);
                });

        addPredicate(
                EnchantmentSelectListener.class,
                EnchantmentSelectEvent.class,
                (listener, event) -> {
                    EnchantmentPosition[] positions = listener.value();
                    if(positions == null || positions.length == 0)
                        return true;

                    return contains(positions, event.getEnchantmentPosition());
                });

        addPredicate(
                LecternButtonClickListener.class,
                LecternButtonClickEvent.class,
                (listener, event) -> {
                    LecternButtonType[] buttons = listener.value();
                    if(buttons == null || buttons.length == 0)
                        return true;

                    return contains(buttons, event.getButtonType());
                });

        addPredicate(
                LecternPageOpenListener.class,
                LecternPageOpenEvent.class,
                (listener, event) -> {
                    int[] pages = listener.value();
                    if(pages == null || pages.length == 0)
                        return true;

                    return contains(pages, event.getPage());
                });

        addPredicate(
                PatternSelectListener.class,
                PatternSelectEvent.class,
                (listener, event) -> {
                    int[] slots = listener.value();
                    if(slots == null || slots.length == 0)
                        return true;

                    return contains(slots, event.getSlot());
                });

        addPredicate(
                RecipeSelectListener.class,
                RecipeSelectEvent.class,
                (listener, event) -> {
                    int[] slots = listener.value();
                    if(slots == null || slots.length == 0)
                        return true;

                    return contains(slots, event.getSlot());
                });

        addPredicate(
                TradeSelectListener.class,
                TradeSelectEvent.class,
                (listener, event) -> {
                    int[] slots = listener.value();
                    if(slots == null || slots.length == 0)
                        return true;

                    return contains(slots, event.getSelectedSlot());
                });
    }

}
