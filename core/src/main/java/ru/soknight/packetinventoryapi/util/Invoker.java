package ru.soknight.packetinventoryapi.util;

@FunctionalInterface
public interface Invoker<T> {

    T invoke(Object... args);

}
