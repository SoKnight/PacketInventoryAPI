package ru.soknight.packetinventoryapi.container.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;

public class SmokerFurnaceContainer extends FurnaceContainer<SmokerFurnaceContainer> {

    private SmokerFurnaceContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.SMOKER, title);
    }

    private SmokerFurnaceContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.SMOKER, title);
    }

    public static SmokerFurnaceContainer create(Player inventoryHolder, String title) {
        return new SmokerFurnaceContainer(inventoryHolder, title);
    }

    public static SmokerFurnaceContainer create(Player inventoryHolder, BaseComponent title) {
        return new SmokerFurnaceContainer(inventoryHolder, title);
    }

    public static SmokerFurnaceContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.SMOKER);
    }

    @Override
    protected SmokerFurnaceContainer getThis() {
        return this;
    }

    @Override
    public SmokerFurnaceContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

}
