package ru.soknight.packetinventoryapi.storage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;

public interface ContainerStorage {

    static ContainerStorage create() {
        return new SimpleContainerStorage();
    }

    int INVENTORY_ID = 112;
    ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    void open(Container<?, ?> container);

    boolean close(Player holder);

    void close(Container<?, ?> container);

}
