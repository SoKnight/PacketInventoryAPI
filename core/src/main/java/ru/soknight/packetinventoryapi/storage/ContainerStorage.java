package ru.soknight.packetinventoryapi.storage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.container.Container;

public interface ContainerStorage {

    static @NotNull ContainerStorage create() {
        return new SimpleContainerStorage();
    }

    int INVENTORY_ID = 112;
    ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    Container<?, ?> getOpened(@NotNull String holderName);

    boolean isViewing(@NotNull String holderName);

    boolean isViewing(@NotNull Container<?, ?> container);

    void open(@NotNull Container<?, ?> container);

    boolean close(@NotNull String holderName);

    /**
     * @deprecated Use {@link #close(String)} instead.
     */
    @Deprecated
    boolean close(@NotNull Player holder);

    void close(@NotNull Container<?, ?> container);

}
