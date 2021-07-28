package ru.soknight.packetinventoryapi.menu.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Context {

    @NotNull Player getViewer();

}
