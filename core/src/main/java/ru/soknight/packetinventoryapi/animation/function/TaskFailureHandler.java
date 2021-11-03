package ru.soknight.packetinventoryapi.animation.function;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface TaskFailureHandler {

    void handle(Player viewer, Throwable throwable);

}
