package ru.soknight.packetinventoryapi.listener.event;

import ru.soknight.packetinventoryapi.event.Event;

@FunctionalInterface
public interface EventListener<E extends Event<?, ?>> {

    void handle(E event);
    
}
