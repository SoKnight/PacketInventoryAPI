package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerTypes;

public class BlastFurnaceContainer extends FurnaceContainer<BlastFurnaceContainer> {

    private BlastFurnaceContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.BLAST_FURNACE, title);
    }

    private BlastFurnaceContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.BLAST_FURNACE, title);
    }

    public static @NotNull BlastFurnaceContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new BlastFurnaceContainer(inventoryHolder, title);
    }

    public static @NotNull BlastFurnaceContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new BlastFurnaceContainer(inventoryHolder, title);
    }

    public static @NotNull BlastFurnaceContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.BLAST_FURNACE);
    }

    @Override
    protected @NotNull BlastFurnaceContainer getThis() {
        return this;
    }

    @Override
    public @NotNull BlastFurnaceContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }
    
}
