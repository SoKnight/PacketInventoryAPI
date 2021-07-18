package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;

public class BlastFurnaceContainer extends FurnaceContainer<BlastFurnaceContainer> {

    private BlastFurnaceContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.BLAST_FURNACE, title);
    }

    private BlastFurnaceContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.BLAST_FURNACE, title);
    }

    public static BlastFurnaceContainer create(Player inventoryHolder, String title) {
        return new BlastFurnaceContainer(inventoryHolder, title);
    }

    public static BlastFurnaceContainer create(Player inventoryHolder, BaseComponent title) {
        return new BlastFurnaceContainer(inventoryHolder, title);
    }

    public static BlastFurnaceContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.BLAST_FURNACE);
    }

    @Override
    protected BlastFurnaceContainer getThis() {
        return this;
    }

    @Override
    public BlastFurnaceContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }
    
}
