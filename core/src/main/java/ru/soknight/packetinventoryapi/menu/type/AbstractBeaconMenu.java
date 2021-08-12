package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import ru.soknight.packetinventoryapi.annotation.container.BeaconEffectChangeListener;
import ru.soknight.packetinventoryapi.container.type.BeaconContainer;
import ru.soknight.packetinventoryapi.event.container.BeaconEffectChangeEvent;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractBeaconMenu extends AbstractMenu<BeaconContainer, BeaconContainer.BeaconUpdateRequest> {

    public AbstractBeaconMenu(String name, Plugin providingPlugin) {
        this(BeaconContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractBeaconMenu(String name, Plugin providingPlugin, String title) {
        this(BeaconContainer.create(null, title), name, providingPlugin);
    }

    public AbstractBeaconMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(BeaconContainer.create(null, title), name, providingPlugin);
    }

    public AbstractBeaconMenu(BeaconContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    @BeaconEffectChangeListener
    public void onEffectChange(BeaconEffectChangeEvent event) {
        onEffectChange(
                event.getActor(),
                event.getContainer(),
                event.getPrimaryEffect(),
                event.getSecondaryEffect()
        );
    }

    protected void onEffectChange(
            Player actor,
            BeaconContainer container,
            PotionEffectType primary,
            PotionEffectType secondary
    ) {}

}
