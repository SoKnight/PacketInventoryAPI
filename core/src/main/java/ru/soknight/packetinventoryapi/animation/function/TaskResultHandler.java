package ru.soknight.packetinventoryapi.animation.function;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface TaskResultHandler<T> {

    void handle(Player viewer, T result);

}
