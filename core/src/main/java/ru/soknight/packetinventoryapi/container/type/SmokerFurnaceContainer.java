package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerTypes;

public class SmokerFurnaceContainer extends FurnaceContainer<SmokerFurnaceContainer> {

    private SmokerFurnaceContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.SMOKER, title);
    }

    private SmokerFurnaceContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.SMOKER, title);
    }

    public static @NotNull SmokerFurnaceContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new SmokerFurnaceContainer(inventoryHolder, title);
    }

    public static @NotNull SmokerFurnaceContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new SmokerFurnaceContainer(inventoryHolder, title);
    }

    public static @NotNull SmokerFurnaceContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.SMOKER);
    }

    @Override
    protected @NotNull SmokerFurnaceContainer getThis() {
        return this;
    }

    @Override
    public @NotNull SmokerFurnaceContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

}
