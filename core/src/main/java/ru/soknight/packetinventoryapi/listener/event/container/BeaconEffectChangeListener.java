package ru.soknight.packetinventoryapi.listener.event.container;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import ru.soknight.packetinventoryapi.container.type.BeaconContainer;
import ru.soknight.packetinventoryapi.event.container.BeaconEffectChangeEvent;
import ru.soknight.packetinventoryapi.listener.event.EventListener;

@FunctionalInterface
public interface BeaconEffectChangeListener extends EventListener<BeaconEffectChangeEvent> {

    @Override
    default void handle(BeaconEffectChangeEvent event) {
        handle(event.getActor(), event.getContainer(), event.getPrimaryEffect(), event.getSecondaryEffect());
    }
    
    void handle(
            Player actor,
            BeaconContainer container,
            PotionEffectType primaryEffect,
            PotionEffectType secondaryEffect
    );
    
}
