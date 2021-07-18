package ru.soknight.packetinventoryapi.event.container;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import ru.soknight.packetinventoryapi.container.type.BeaconContainer;
import ru.soknight.packetinventoryapi.event.Event;

@Getter
public class BeaconEffectChangeEvent extends Event<BeaconContainer, BeaconContainer.BeaconUpdateRequest> {

    private final PotionEffectType primaryEffect;
    private final PotionEffectType secondaryEffect;

    public BeaconEffectChangeEvent(
            Player actor,
            PotionEffectType primaryEffect,
            PotionEffectType secondaryEffect
    ) {
        this(actor, null, primaryEffect, secondaryEffect);
    }

    public BeaconEffectChangeEvent(
            Player actor,
            BeaconContainer container,
            PotionEffectType primaryEffect,
            PotionEffectType secondaryEffect
    ) {
        super(actor, container);
        this.primaryEffect = primaryEffect;
        this.secondaryEffect = secondaryEffect;
    }

    @SuppressWarnings("deprecation")
    public BeaconEffectChangeEvent(
            Player actor,
            BeaconContainer container,
            int primaryEffectId,
            int secondaryEffectId
    ) {
        super(actor, container);
        
        this.primaryEffect = PotionEffectType.getById(primaryEffectId);
        this.secondaryEffect = PotionEffectType.getById(secondaryEffectId);
    }

    @Override
    public String toString() {
        return "BeaconEffectChangeEvent{" +
                "actor=" + actor +
                ", container=" + container +
                ", primaryEffect=" + primaryEffect +
                ", secondaryEffect=" + secondaryEffect +
                '}';
    }

}
