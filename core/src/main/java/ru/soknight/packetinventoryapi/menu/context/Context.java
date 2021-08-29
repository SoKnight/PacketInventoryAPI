package ru.soknight.packetinventoryapi.menu.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface Context {

    @Nullable Player getViewer();

}
